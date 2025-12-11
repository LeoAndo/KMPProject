package org.example.project

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kmpproject.composeapp.generated.resources.Res
import kmpproject.composeapp.generated.resources.compose_multiplatform
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * アプリ全体のエントリーポイントとなるComposable。
 */
@Composable
@Preview
fun App() {
    // MaterialThemeで全体の色やタイポグラフィを適用する
    MaterialTheme {
        // ボタン押下による表示切り替え状態を保持（プロセス再生成でも復元される）
        var showContent by rememberSaveable { mutableStateOf(false) }
        // ボタン押下回数を記録するカウンター
        var count by rememberSaveable { mutableStateOf(0) }
        Column(
            modifier = Modifier
                .safeContentPadding()
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // 現在のカウント値を表示
            Text("count: $count")
            // 押下で表示切り替えとカウント増加を行うボタン
            Button(onClick = {
                showContent = !showContent
                count++
            }) {
                Text("Click me!")
            }
            // showContentがtrueの時のみ挨拶と画像をフェード表示
            AnimatedVisibility(showContent) {
                // プラットフォーム別の挨拶文を生成
                val greeting = Greeting().greet()
                Column(
                    Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    // Compose Multiplatformのロゴを表示
                    Image(painterResource(Res.drawable.compose_multiplatform), null)
                    // 挨拶文を表示
                    Text("Compose: $greeting")
                }
            }
        }
    }
}
