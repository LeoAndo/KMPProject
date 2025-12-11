package org.example.project

/**
 * クイズに出題する問題と選択肢をまとめたEnum。
 *
 * 利用例:
 * ```
 * // 最初の問題を取得して、正解かどうかを判定する
 * val firstQuestion = Question.Q1
 * println(firstQuestion.message) // 問題文を表示する
 * val isCorrect = firstQuestion.answerIndex == 0 // 正解のインデックスと照合
 * val firstChoice = firstQuestion.answers[firstQuestion.answerIndex] // 正解の文字列を取り出す
 * ```
 */
enum class Question(
    val message: String, // 表示する問題文
    val answers: List<String>, // 選択肢の一覧
    val answerIndex: Int // answers の中で正解となるインデックス
) {
    Q1(
        message = "Androidの開発言語は？",
        answers = listOf("Java", "PHP", "Ruby", "Go", "Swift"),
        answerIndex = 0
    ),
    Q2(
        message = "iOSの開発言語は？",
        answers = listOf("PHP", "Swift", "Ruby"),
        answerIndex = 1
    ),
    Q3(
        message = "デザインツールは？",
        answers = listOf("word", "Xcode", "Figma", "Excel"),
        answerIndex = 2
    ),
    Q4(
        message = "存在する専門学校は？",
        answers = listOf("日本電子", "アジア電子", "ヨーロッパ電子", "南アフリカ電子"),
        answerIndex = 0
    ),
}
