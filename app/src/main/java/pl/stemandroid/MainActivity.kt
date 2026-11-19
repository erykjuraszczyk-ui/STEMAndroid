package pl.stemandroid

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Gamepad
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID

private val StemColors = darkColorScheme(
    primary = Color(0xFF80D9FF),
    secondary = Color(0xFF9BE7C4),
    background = Color(0xFF0A101A),
    surface = Color(0xFF141E2B),
    onPrimary = Color(0xFF001F2A),
    onBackground = Color(0xFFE8F0F7),
    onSurface = Color(0xFFE8F0F7)
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(colorScheme = StemColors) {
                StemHome()
            }
        }
    }
}

private data class StemGame(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val steamAppId: String,
    val executablePath: String
)

@Composable
private fun StemHome() {
    val context = LocalContext.current
    var games by remember { mutableStateOf(GameStorage.load(context)) }
    var showAddDialog by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 18.dp, end = 18.dp, top = 22.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                HeaderBlock(gameCount = games.size)
            }

            item {
                ModeCard(
                    title = "TRYB LOKALNY",
                    subtitle = "Wine + Box64 przez Winlator",
                    description = "Gra działa na telefonie, o ile jest zgodna z warstwą Windows i sterownikiem GPU.",
                    actionText = "Otwórz Winlator",
                    onClick = { RunnerActions.openWinlator(context) }
                )
            }

            item {
                ModeCard(
                    title = "TRYB ZDALNY",
                    subtitle = "Steam Remote Play / Steam Link",
                    description = "Gra działa na komputerze, a telefon odbiera obraz i wysyła sterowanie.",
                    actionText = "Otwórz Steam Link",
                    onClick = { RunnerActions.openSteamLink(context) }
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "MOJA BIBLIOTEKA",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Lokalna lista gier i ścieżek EXE",
                            color = Color(0xFF9DAFBE),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    Button(onClick = { showAddDialog = true }) {
                        Icon(Icons.Default.Add, contentDescription = null)
                        Spacer(Modifier.width(6.dp))
                        Text("Dodaj")
                    }
                }
            }

            if (games.isEmpty()) {
                item {
                    EmptyLibraryCard(onAdd = { showAddDialog = true })
                }
            } else {
                items(games, key = { it.id }) { game ->
                    GameCard(
                        game = game,
                        onOpenRunner = { RunnerActions.openGameInWinlator(context, game.executablePath) },
                        onOpenStore = {
                            if (game.steamAppId.isBlank()) {
                                Toast.makeText(context, "Dodaj Steam App ID tej gry.", Toast.LENGTH_SHORT).show()
                            } else {
                                RunnerActions.openSteamStore(context, game.steamAppId)
                            }
                        },
                        onDelete = {
                            games = games.filterNot { it.id == game.id }
                            GameStorage.save(context, games)
                        }
                    )
                }
            }

            item {
                Text(
                    text = "STEMAndroid 0.1.0 • Fanowski projekt, niezwiązany z Valve",
                    color = Color(0xFF718394),
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }

    if (showAddDialog) {
        AddGameDialog(
            onDismiss = { showAddDialog = false },
            onAdd = { title, appId, exePath ->
                val newGame = StemGame(
                    title = title.trim(),
                    steamAppId = appId.trim(),
                    executablePath = exePath.trim()
                )
                games = games + newGame
                GameStorage.save(context, games)
                showAddDialog = false
            }
        )
    }
}

@Composable
private fun HeaderBlock(gameCount: Int) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.linearGradient(
                    listOf(Color(0xFF153A55), Color(0xFF172337), Color(0xFF172B31))
                ),
                shape = RoundedCornerShape(22.dp)
            )
            .padding(20.dp)
    ) {
        Text(
            text = "STEMANDROID",
            color = Color(0xFF91E1FF),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Black
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Twoja biblioteka Steam.\nNa Androidzie.",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Spacer(Modifier.height(10.dp))
        Text(
            text = "Android 13+  •  ARM64  •  Lokalnie i zdalnie",
            color = Color(0xFFC1D5E3),
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(Modifier.height(16.dp))
        Divider(color = Color(0xFF476276))
        Spacer(Modifier.height(12.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Gamepad, contentDescription = null, tint = Color(0xFF91E1FF))
            Spacer(Modifier.width(8.dp))
            Text(
                text = "Gry w bibliotece: " + gameCount,
                color = Color.White,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun ModeCard(
    title: String,
    subtitle: String,
    description: String,
    actionText: String,
    onClick: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(18.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(title, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(4.dp))
            Text(subtitle, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(6.dp))
            Text(description, color = Color(0xFFB5C2CE), style = MaterialTheme.typography.bodySmall)
            Spacer(Modifier.height(12.dp))
            OutlinedButton(onClick = onClick) {
                Text(actionText)
                Spacer(Modifier.width(8.dp))
                Icon(Icons.Default.OpenInNew, contentDescription = null)
            }
        }
    }
}

@Composable
private fun EmptyLibraryCard(onAdd: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF111A26)),
        shape = RoundedCornerShape(18.dp)
    ) {
        Column(
            modifier = Modifier.padding(22.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(Icons.Default.Gamepad, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.height(8.dp))
            Text("Biblioteka jest pusta", fontWeight = FontWeight.Bold)
            Text(
                "Dodaj pierwszą grę, Steam App ID i ścieżkę pliku EXE w kontenerze Winlator.",
                color = Color(0xFF9DAFBE),
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(Modifier.height(12.dp))
            Button(onClick = onAdd) { Text("Dodaj pierwszą grę") }
        }
    }
}

@Composable
private fun GameCard(
    game: StemGame,
    onOpenRunner: () -> Unit,
    onOpenStore: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF141E2B)),
        shape = RoundedCornerShape(18.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Column(Modifier.weight(1f)) {
                    Text(
                        text = game.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = if (game.steamAppId.isBlank()) "Steam App ID: nie podano" else "Steam App ID: " + game.steamAppId,
                        color = Color(0xFF9DAFBE),
                        style = MaterialTheme.typography.labelSmall
                    )
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Usuń grę", tint = Color(0xFFFF9B9B))
                }
            }
            Spacer(Modifier.height(6.dp))
            Text(
                text = game.executablePath.ifBlank { "Nie podano ścieżki EXE" },
                color = Color(0xFFB5C2CE),
                style = MaterialTheme.typography.bodySmall,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = onOpenRunner, modifier = Modifier.weight(1f)) {
                    Text("Otwórz Winlator")
                }
                OutlinedButton(onClick = onOpenStore, modifier = Modifier.weight(1f)) {
                    Text("Steam Store")
                }
            }
        }
    }
}

@Composable
private fun AddGameDialog(
    onDismiss: () -> Unit,
    onAdd: (String, String, String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var appId by remember { mutableStateOf("") }
    var exePath by remember { mutableStateOf("") }
    var validationMessage by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Dodaj grę") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Nazwa gry") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = appId,
                    onValueChange = { appId = it.filter(Char::isDigit) },
                    label = { Text("Steam App ID (opcjonalnie)") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = exePath,
                    onValueChange = { exePath = it },
                    label = { Text("Ścieżka EXE w Winlatorze") },
                    placeholder = { Text("C:\\Games\\Game\\game.exe") },
                    minLines = 2
                )
                if (validationMessage.isNotBlank()) {
                    Text(validationMessage, color = MaterialTheme.colorScheme.error)
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (title.isBlank()) {
                        validationMessage = "Podaj nazwę gry."
                    } else {
                        onAdd(title, appId, exePath)
                    }
                }
            ) { Text("Zapisz") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Anuluj") }
        }
    )
}

private object GameStorage {
    private const val PREFS_NAME = "stem_android_library"
    private const val GAMES_KEY = "games_json"

    fun load(context: Context): List<StemGame> {
        val raw = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getString(GAMES_KEY, "[]") ?: "[]"

        return try {
            val array = JSONArray(raw)
            buildList {
                for (index in 0 until array.length()) {
                    val item = array.optJSONObject(index) ?: continue
                    val title = item.optString("title").trim()
                    if (title.isNotEmpty()) {
                        add(
                            StemGame(
                                id = item.optString("id").ifBlank { UUID.randomUUID().toString() },
                                title = title,
                                steamAppId = item.optString("steamAppId"),
                                executablePath = item.optString("executablePath")
                            )
                        )
                    }
                }
            }
        } catch (_: Exception) {
            emptyList()
        }
    }

    fun save(context: Context, games: List<StemGame>) {
        val array = JSONArray()
        games.forEach { game ->
            array.put(
                JSONObject()
                    .put("id", game.id)
                    .put("title", game.title)
                    .put("steamAppId", game.steamAppId)
                    .put("executablePath", game.executablePath)
            )
        }
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(GAMES_KEY, array.toString())
            .apply()
    }
}

private object RunnerActions {
    private const val WINLATOR_PACKAGE = "com.winlator"
    private const val STEAM_LINK_PACKAGE = "com.valvesoftware.steamlink"

    fun openWinlator(context: Context) {
        val launchIntent = context.packageManager.getLaunchIntentForPackage(WINLATOR_PACKAGE)
        if (launchIntent != null) {
            context.startActivity(launchIntent)
        } else {
            openUrl(context, "https://winlator.org/")
            Toast.makeText(
                context,
                "Nie wykryto Winlatora. Otwieram jego oficjalną stronę.",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    fun openGameInWinlator(context: Context, executablePath: String) {
        if (executablePath.isBlank()) {
            Toast.makeText(context, "Najpierw uzupełnij ścieżkę EXE w edycji gry.", Toast.LENGTH_LONG).show()
            return
        }

        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.setPrimaryClip(ClipData.newPlainText("Ścieżka EXE", executablePath))

        val launchIntent = context.packageManager.getLaunchIntentForPackage(WINLATOR_PACKAGE)
        if (launchIntent != null) {
            context.startActivity(launchIntent)
            Toast.makeText(
                context,
                "Otworzono Winlator. Ścieżka EXE została skopiowana. Uruchom ją w kontenerze.",
                Toast.LENGTH_LONG
            ).show()
        } else {
            openUrl(context, "https://winlator.org/")
            Toast.makeText(
                context,
                "Nie wykryto Winlatora. Ścieżka EXE została skopiowana do schowka.",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    fun openSteamLink(context: Context) {
        val launchIntent = context.packageManager.getLaunchIntentForPackage(STEAM_LINK_PACKAGE)
        if (launchIntent != null) {
            context.startActivity(launchIntent)
        } else {
            openUrl(
                context,
                "https://play.google.com/store/apps/details?id=com.valvesoftware.steamlink"
            )
        }
    }

    fun openSteamStore(context: Context, appId: String) {
        if (!appId.matches(Regex("[0-9]+"))) {
            Toast.makeText(context, "Steam App ID musi zawierać tylko cyfry.", Toast.LENGTH_SHORT).show()
            return
        }
        openUrl(context, "https://store.steampowered.com/app/" + appId + "/")
    }

    private fun openUrl(context: Context, url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        try {
            context.startActivity(intent)
        } catch (_: Exception) {
            Toast.makeText(context, "Brak aplikacji do otwarcia tego adresu.", Toast.LENGTH_SHORT).show()
        }
    }
}
