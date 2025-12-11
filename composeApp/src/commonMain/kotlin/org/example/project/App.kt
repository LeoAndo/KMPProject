package org.example.project

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * アプリケーションのメインComposable。
 * AppTheme配下でクイズ画面全体を構成し、進行状態・結果表示・通知をまとめて扱う。
 *
 * 利用例:
 * ```
 * @Composable
 * fun Root() {
 *     App() // そのまま呼び出すだけでクイズ画面を描画
 * }
 * ```
 */
@Composable
@Preview
fun App() {
    AppTheme {
        // クイズの進行・通知・スクロール状態を Compose スコープ内で保持する。
        val hostState = remember { SnackbarHostState() }
        // 出題位置を保持するページャー。ユーザーが自由にスワイプしないようスクロール不可にしている。
        val pagerState = rememberPagerState(pageCount = { Question.entries.size })
        // ページ遷移やスナックバー表示を非同期で行うためのスコープ。
        val scope = rememberCoroutineScope()
        // 縦方向のスクロール状態。小さい画面でも縦スクロールで全要素を確認できる。
        val scrollState = rememberScrollState()
        // 最終問題まで回答したかどうか。
        var isFinishedQuiz by remember { mutableStateOf(false) }
        // 現時点での正解数。
        var collectAnswerCount by remember { mutableStateOf(0) }

        // 画面骨組み。Snackbar の描画位置や全体の余白を提供する。
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            snackbarHost = { SnackbarHost(hostState = hostState) },
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding).padding(12.dp)
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                // 出題ページ: 現在のインデックスに対応する問題文を表示。
                HorizontalPager(
                    state = pagerState,
                    userScrollEnabled = false,
                ) { pageIndex ->
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.5f)
                                .aspectRatio(1f)
                                .background(color = MaterialTheme.colorScheme.secondaryContainer)
                                .padding(12.dp), // 文字の周りに余白を追加する
                            contentAlignment = Alignment.Center,
                        ) {
                            // ordinal で現在ページに一致する問題文を取得。
                            val message = Question.entries.first { it.ordinal == pageIndex }.message
                            Text(
                                text = message, // TODO 仮でテキストにページインデックスを表示する
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                    }
                }

                if (isFinishedQuiz) {
                    // 全問回答後のリザルト表示とリセット導線。
                    Text("${collectAnswerCount}問正解しました")
                    Button(onClick = {
                        scope.launch {
                            pagerState.scrollToPage(page = 0)
                            collectAnswerCount = 0
                            isFinishedQuiz = false
                        }
                    }) {
                        Text("やり直す")
                    }
                }

                // ページャーの現在位置に対応する問題を選択肢描画のために取得。
                val question = Question.entries
                    .first { question -> question.ordinal == pagerState.currentPage }
                question.answers.forEachIndexed { index, answerText ->
                    // 選択肢ボタン: 正誤判定しスナックバー表示後、必要に応じて次ページへ遷移。
                    Button(
                        enabled = !isFinishedQuiz,
                        modifier = Modifier.widthIn(max = 320.dp).fillMaxWidth(0.5f),
                        onClick = {
                            scope.launch {
                                isFinishedQuiz =
                                    Question.entries.size <= pagerState.currentPage + 1
                                if (!isFinishedQuiz) {
                                    pagerState.scrollToPage(page = pagerState.currentPage + 1)
                                }
                                val message = if (question.answerIndex == index) {
                                    collectAnswerCount++
                                    "正解です！"
                                } else {
                                    "不正解です！"
                                }
                                // 連続押下時でも最新のメッセージだけが表示されるように既存を閉じてから表示する。
                                hostState.currentSnackbarData?.dismiss()
                                hostState.showSnackbar(message)
                            }
                        },
                    ) {
                        Text(
                            text = answerText,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
            }
        }
    }
}
