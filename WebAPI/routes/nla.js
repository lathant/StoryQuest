const router = require("express").Router();
const language = require("@google-cloud/language");
const dotenv = require("dotenv");
dotenv.config();

/* GET users listing. */
router.get("/", function(req, res, next) {
  if (!req.query.text) {
    return res.sendStatus(400);
  }
  analyze(req.query.text)
    .then(result => res.send(result.join("|")))
    .catch(err => {
      console.error(err);
      res.sendStatus(500);
    });
});

/**
 *
 * We can use this data to infer the emotion of the child's text
 * This way, in conjunction with the "reply" API, we can not only have
 *   and infinite chat, but also reward more positive responses from the child
 *
 */

async function analyze(text) {
  // Instantiates a client
  const client = new language.LanguageServiceClient({
    credentials: {
      client_email: process.env.CLIENT_EMAIL,
      private_key: process.env.PRIVATE_KEY
    }
  });

  const document = {
    content: text,
    type: "PLAIN_TEXT"
  };

  console.log(
    `\nText: ${text.length > 90 ? text.substr(0, 87) + "..." : text}`
  );

  // Extract emotion from the text
  let sentimentRatio = await analyzeSentiment(client, document);
  console.log(`Ratio: ${sentimentRatio}`);
  let emotion = await parseSentimentRatio(sentimentRatio);
  console.log(`Result: ${emotion}`);

  let context = (await analyzeContext(client, document))
    .map(c => `${c.name.replace("/", "")}`) //:${(c.confidence * 100).toFixed(1)
    .join(",");

  // Extract entities (and their salience) from the text
  let entities = (await analyzeEntities(client, document))
    .reduce((acc, cur) => {
      let i = acc.findIndex(e => e[0] == cur.type);
      if (i < 0) {
        acc.push([cur.type, 1]);
      } else {
        acc[i][1]++;
      }
      return acc;
    }, [])
    .map(eg => eg[0]) // eg.join(".")
    .join(",");
  console.log(`Entities: ${entities}`);

  return [emotion, context, entities];
}

/**
 * @param {language.LanguageServiceClient} client
 * @param {*} document
 */
async function analyzeEntities(client, document) {
  // Detects entities in the document
  const [result] = await client.analyzeEntities({ document });

  const entities = result.entities;
  let entityResult = [];

  console.log("Entities:");
  entities.forEach(entity => {
    console.log(entity.name);
    console.log(` - Type: ${entity.type}, Salience: ${entity.salience}`);
    if (entity.metadata && entity.metadata.wikipedia_url) {
      console.log(` - Wikipedia URL: ${entity.metadata.wikipedia_url}`);
    }

    entityResult.push({
      name: entity.name,
      type: entity.type,
      salience: entity.salience
    });
  });

  return entityResult;
}

/**
 * @param {language.LanguageServiceClient} client
 * @param {*} document
 */
async function analyzeContext(client, document) {
  // Classifies text in the document
  const [classification] = await client.classifyText({ document });
  console.log("Categories:");
  classification.categories.forEach(category => {
    console.log(`Name: ${category.name}, Confidence: ${category.confidence}`);
  });

  return classification.categories;
}

/**
 * @param {language.LanguageServiceClient} client
 * @param {*} document
 */
async function analyzeSentiment(client, document) {
  // Detects the sentiment (emotion) of the text
  const [sentimentResult] = await client.analyzeSentiment({ document });
  const sentiment = sentimentResult.documentSentiment;

  // sentiment score is the actual emotion >0 = +ve emotion, <0 = -ve emotion, 0 = neutral
  console.log(`Sentiment score: ${sentiment.score}`);
  // sentiment magnitude is the 'magnitude' of the emotion, greater numbers = stronger emotion
  console.log(`Sentiment magnitude: ${sentiment.magnitude}`);

  return sentiment.score * sentiment.magnitude;
}

function parseSentimentRatio(ratio) {
  /**
   *      Theme           score   magnitude    ratio (score*magnitude||1)
   * Clearly positive     0.8     3        >=  1
   * positive             0.8     3        <   1
   *                      0.4     1.5      >=  0.6
   * Mildly-positive      0.4     1.5      <   0.6
   *                      0.2     1.5      >=  0.4
   * neutral              0.2     1.5      <   0.4
   *                      0.1     1        >= -0.4
   * Mildly-negative      0.1     1        <  -0.4
   *                     -0.3     1.5      >= -1
   * Negative            -0.3     1.5      <  -1
   *                     -0.6     3        >= -1.5
   * Clearly Negative    -0.6     3        <  -1.5
   */

  if (ratio >= 1) {
    return "positive+";
  } else if (ratio >= 0.6) {
    return "positive";
  } else if (ratio >= 0.4) {
    return "positive-";
  } else if (ratio >= -0.4) {
    return "neutral";
  } else if (ratio >= -1) {
    return "negative-";
  } else if (ratio >= -1.5) {
    return "negative";
  } else {
    return "negative+";
  }
}

module.exports = router;
