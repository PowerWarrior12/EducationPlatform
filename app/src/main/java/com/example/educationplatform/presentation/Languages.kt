package com.example.educationplatform.presentation

import de.markusressel.kodehighlighter.core.LanguageRuleBook
import de.markusressel.kodehighlighter.language.java.JavaRuleBook
import de.markusressel.kodehighlighter.language.kotlin.KotlinRuleBook
import de.markusressel.kodehighlighter.language.markdown.MarkdownRuleBook
import de.markusressel.kodehighlighter.language.python.PythonRuleBook

val languagesMap = mapOf<String, LanguageRuleBook>(
    "java" to JavaRuleBook(),
    "cpp" to MarkdownRuleBook(),
    "php" to MarkdownRuleBook(),
    "python3" to PythonRuleBook(),
    "ruby" to MarkdownRuleBook(),
    "scala" to MarkdownRuleBook(),
    "sql" to MarkdownRuleBook(),
    "pascal" to MarkdownRuleBook(),
    "csharp" to MarkdownRuleBook(),
    "groovy" to MarkdownRuleBook(),
    "rust" to MarkdownRuleBook(),
    "dart" to MarkdownRuleBook(),
    "nodejs" to MarkdownRuleBook(),
    "kotlin" to KotlinRuleBook()
)