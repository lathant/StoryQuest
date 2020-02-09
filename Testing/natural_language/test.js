async function quickstart() {
  // Imports the Google Cloud client library
  const language = require("@google-cloud/language");

  // Instantiates a client
  const client = new language.LanguageServiceClient();

  // The text to analyze
  const text = "Hello, world!";

  const document = {
    content: text,
    type: "PLAIN_TEXT"
  };

  // Detects the sentiment (emotion) of the text
  const [result] = await client.analyzeSentiment({ document: document });
  const sentiment = result.documentSentiment;

  console.log(`Text: ${text}`);
  // sentiment score is the actual emotion >0 = +ve emotion, <0 = -ve emotion, 0 = neutral
  console.log(`Sentiment score: ${sentiment.score}`);
  // sentiment magnitude is the 'magnitude' of the emotion, greater numbers = stronger emotion
  console.log(`Sentiment magnitude: ${sentiment.magnitude}`);

  /**
   *      Theme           score   magnitude    ratio (score*magnitude||1)
   * Clearly positive     0.8     3        >=  2.4
   * positive             0.8     3        <   2.4
   *                      0.4     1.5      >=  0.6
   * Mildly-positive      0.4     1.5      <   0.6
   *                      0.2     1.5      >=  0.3
   * neutral              0.2     1.5      <   0.3
   *                      0.1     1        >=  0.1
   * Mildly-negative      0.1     1        <   0.1
   *                     -0.3     1.5      >= -0.45
   * Negative            -0.3     1.5      <  -0.45
   *                     -0.6     3        >= -1.8
   * Clearly Negative    -0.6     3        <  -1.8
   * 
   * 
   * 
   * We can use this data to infer the emotion of the child's text
   * This way, in conjunction with the "reply" API, we can not only have
   *   and infinite chat, but also reward more positive responses from the child
   * 
   */
}

quickstart()
  .then(() => console.log("\n done :) \n"))
  .catch(err => console.error(err));
