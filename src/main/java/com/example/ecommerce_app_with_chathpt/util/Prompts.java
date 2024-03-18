package com.example.ecommerce_app_with_chathpt.util;

public class Prompts {

    public static final String INTENT_EXTRACTION_PROMPT = "These are intents of my sentences = [search, login, register, buy, other]. If the intent of the message I give you is one of these, return just the intent of that sentence."
            + "\nExample: 'I want to search for books.' Output: search"
            + "\nExample: 'How do I log in to my account?' Output: login"
            + "\nExample: 'I need to register for a new account.' Output: register"
            + "\nExample: 'I'd like to buy a new laptop.' Output: buy"
            + "\nExample: 'Tell me more about your services.' Output: other";


}
