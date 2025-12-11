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
 * アプリケーションのメインComposable関数。
 * UIの状態を保持し、ユーザーインタラクションに応じてコンテンツの表示を切り替えます。
 * この関数は、MaterialTheme内でUIを構築し、`rememberSaveable`を使用して状態を維持します。
 */
@Composable
@Preview
fun App() {
    MaterialTheme {
        // クイズの進行・通知・スクロール状態を Compose スコープ内で保持する。
        val hostState = remember { SnackbarHostState() }
        val pagerState =
            rememberPagerState(pageCount = { Question.entries.size })
        val scope = rememberCoroutineScope()
        val scrollState = rememberScrollState()
        var isFinishedQuiz by remember { mutableStateOf(false) }
        var collectAnswerCount by remember { mutableStateOf(0) }

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            snackbarHost = { SnackbarHost(hostState = hostState) }
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
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.5f)
                                .aspectRatio(1f)
                                .background(color = MaterialTheme.colorScheme.secondaryContainer)
                                .padding(12.dp), // 文字の周りに余白を追加する
                            contentAlignment = Alignment.Center
                        ) {
                            val message = Question.entries[pageIndex].message
                            Text(
                                text = message,
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

                val question = Question.entries[pagerState.currentPage]
                question.answers.forEachIndexed { index, answerText ->
                    // 選択肢ボタン: 正誤判定しスナックバー表示後、必要に応じて次ページへ遷移。
                    Button(
                        enabled = !isFinishedQuiz,
                        modifier = Modifier.widthIn(max = 320.dp).fillMaxWidth(0.5f),
                        onClick = {
                            scope.launch {
                                isFinishedQuiz = pagerState.currentPage + 1 == Question.entries.size
                                if (!isFinishedQuiz) {
                                    pagerState.scrollToPage(page = pagerState.currentPage + 1)
                                }
                                val message = if (question.answerIndex == index) {
                                    collectAnswerCount++
                                    "正解です！"
                                } else {
                                    "不正解です！"
                                }
                                hostState.currentSnackbarData?.dismiss()
                                hostState.showSnackbar(message)
                            }
                        },
                    ) {
                        Text(
                            text = answerText,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

            }
        }
    }
}