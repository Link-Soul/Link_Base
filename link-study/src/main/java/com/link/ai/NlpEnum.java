package com.link.ai;


/**
 * @Author Link
 * @Date 2025/5/14 15:19
 */
public enum NlpEnum {
    // AI讲故事
    AI_STORY(1001, "AI讲故事"),
    // 好词好句 sentence
    AI_GOOD_WORDS(1002, "好词好句"),
    // 作文范文
    AI_COMPOSITION(1003, "作文范文"),
    // AI写诗
    AI_POEMS(1004, "AI写诗"),
    // 知识百科
    AI_WIKI(1005, "知识百科"),
    // AI闹钟
    AI_ALARM(1006, "AI闹钟"),
    AI_STUDY_ASSISTANT(1007, "AI助学助手"),
    // AI对话
    AI(1008, "AI对话"),
    AI_TREE_HOLE_ASSISTANT(1009, "心灵树洞"),
    AI_SPEAKER(1010, "AI音箱");

    private final int nlp;
    private final String type;

    NlpEnum(int nlp, String type) {
        this.nlp = nlp;
        this.type = type;
    }

    public int getNlp() {
        return nlp;
    }

    public String getType() {
        return type;
    }



}
