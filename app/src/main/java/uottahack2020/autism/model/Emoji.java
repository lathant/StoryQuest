package uottahack2020.autism.model;

public enum Emoji {

    HAPPY_FACE("😃", ":D");

    private String emoji;
    private String emoticon;

    Emoji(String emoji, String emoticon) {
        this.emoji = emoji;
        this.emoticon = emoticon;
    }

    public String getEmoji() {
        return emoji;
    }

    public String getEmoticon() {
        return emoticon;
    }
}
