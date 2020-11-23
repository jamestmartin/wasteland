package me.jamestmartin.wasteland.manual;

public class ManualConfig {
    private final ManualSection rules;
    private final ManualSection faq;
    
    public ManualConfig(ManualSection rules, ManualSection faq) {
        this.rules = rules;
        this.faq = faq;
    }
    
    public ManualSection getRules() {
        return rules;
    }
    
    public ManualSection getFaq() {
        return faq;
    }
}
