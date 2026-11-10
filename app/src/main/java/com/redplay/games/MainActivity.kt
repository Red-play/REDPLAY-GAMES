
package com.redplay.games

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection

private val Ink = Color(0xFF08090D)
private val Surface1 = Color(0xFF12141B)
private val Surface2 = Color(0xFF1A1D27)
private val Crimson = Color(0xFFFF1744)
private val Pink = Color(0xFFFF5277)
private val Soft = Color(0xFF9DA3B4)
private val White = Color(0xFFF7F7FA)

data class Game(
    val title: String,
    val category: String,
    val desc: String,
    val emoji: String,
    val offline: Boolean,
    val tag: String
)

private val catalog = listOf(
    Game("Dream League Soccer", "كرة القدم", "ابنِ ناديك وطوّر فريقك ونافس في عالم كرة القدم.", "⚽", false, "مشهور"),
    Game("eFootball", "كرة القدم", "تجربة كرة قدم تنافسية.", "🏟️", false, "كرة القدم"),
    Game("EA SPORTS FC Mobile", "كرة القدم", "كوّن فريق أحلامك.", "🏆", false, "مميز"),
    Game("PUBG Mobile", "أكشن", "قاتل للبقاء وكن آخر لاعب صامد.", "🎯", false, "ترند"),
    Game("Free Fire", "أكشن", "معارك بقاء سريعة ومليئة بالإثارة.", "🔥", false, "ترند"),
    Game("Call of Duty Mobile", "أكشن", "أطوار قتال وخرائط متعددة.", "💥", false, "أكشن"),
    Game("Shadow Fight 2", "قتال", "واجه خصومًا أقوياء.", "🥷", true, "بدون نت"),
    Game("Minecraft", "مغامرات", "استكشف وابنِ عوالمك.", "⛏️", true, "مغامرات"),
    Game("Roblox", "مغامرات", "اكتشف تجارب متنوعة.", "🧩", false, "مغامرات"),
    Game("GTA San Andreas", "مغامرات", "قصة عالم مفتوح ومهام ممتعة.", "🚘", true, "كلاسيكية"),
    Game("Asphalt Legends", "سباق", "سيارات خارقة وسباقات سريعة.", "🏎️", false, "سباق"),
    Game("Hill Climb Racing", "سباق", "تسلق التلال وتجاوز العقبات.", "🚙", true, "بدون نت"),
    Game("Traffic Rider", "سباق", "قد دراجتك النارية.", "🏍️", true, "سباق"),
    Game("Subway Surfers", "أركيد", "اركض وتفادى القطارات.", "🚆", true, "مشهورة"),
    Game("Temple Run 2", "أركيد", "اهرب من المعبد واجمع الكنوز.", "🏃", true, "أركيد"),
    Game("Plants vs Zombies", "استراتيجية", "واجه موجات الزومبي.", "🌻", true, "استراتيجية"),
    Game("Angry Birds 2", "أركيد", "استخدم مهارات الطيور.", "🐦", true, "أركيد"),
    Game("Stumble Guys", "أركيد", "تحدى اللاعبين في سباقات.", "🤸", false, "مرح"),
    Game("Vector", "أكشن", "نفذ حركات باركور مذهلة.", "🏃", true, "أكشن"),
    Game("Candy Crush Saga", "ألغاز", "طابق الحلوى واجتز المراحل.", "🍬", true, "ألغاز")
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = android.graphics.Color.rgb(8, 9, 13)
        window.navigationBarColor = android.graphics.Color.rgb(8, 9, 13)
        setContent { RedplayApp() }
    }
}

@Composable
fun RedplayApp() {
    var tab by remember { mutableStateOf("الرئيسية") }
    var search by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("الكل") }
    var selected by remember { mutableStateOf<Game?>(null) }
    val favorites = remember { mutableStateListOf<String>() }

    val categories = listOf(
        "الكل", "كرة القدم", "أكشن", "مغامرات",
        "سباق", "أركيد", "قتال", "ألغاز", "استراتيجية"
    )

    val filtered = catalog.filter {
        (category == "الكل" || it.category == category) &&
        (search.isBlank() ||
            it.title.contains(search, true) ||
            it.category.contains(search, true))
    }

    CompositionLocalProvider(
        LocalLayoutDirection provides LayoutDirection.Rtl
    ) {
        MaterialTheme(
            colorScheme = darkColorScheme(
                primary = Crimson,
                background = Ink,
                surface = Surface1,
                onBackground = White,
                onSurface = White
            )
        ) {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(Ink)
            ) {
                Column(Modifier.fillMaxSize()) {
                    TopBar()

                    AnimatedContent(
                        targetState = selected,
                        label = "screen"
                    ) { game ->
                        if (game != null) {
                            DetailScreen(
                                game,
                                favorites.contains(game.title),
                                {
                                    if (favorites.contains(game.title))
                                        favorites.remove(game.title)
                                    else favorites.add(game.title)
                                },
                                { selected = null }
                            )
                        } else {
                            when (tab) {
                                "الألعاب" -> ListingScreen(
                                    filtered, categories, category,
                                    { category = it }, search, { search = it },
                                    favorites, { selected = it },
                                    {
                                        if (favorites.contains(it))
                                            favorites.remove(it)
                                        else favorites.add(it)
                                    }
                                )

                                "التصنيفات" -> CategoriesScreen(
                                    categories.drop(1)
                                ) {
                                    category = it
                                    tab = "الألعاب"
                                }

                                "المفضلة" -> ListingScreen(
                                    catalog.filter { favorites.contains(it.title) },
                                    categories, category, { category = it },
                                    search, { search = it }, favorites,
                                    { selected = it },
                                    {
                                        if (favorites.contains(it))
                                            favorites.remove(it)
                                        else favorites.add(it)
                                    },
                                    false
                                )

                                "حسابي" -> ProfileScreen()

                                else -> HomeScreen(
                                    favorites,
                                    { selected = it },
                                    {
                                        if (favorites.contains(it))
                                            favorites.remove(it)
                                        else favorites.add(it)
                                    },
                                    search,
                                    { search = it },
                                    { tab = "الألعاب" }
                                )
                            }
                        }
                    }
                }

                if (selected == null) {
                    BottomNav(
                        current = tab,
                        modifier = Modifier.align(Alignment.BottomCenter),
                        select = { tab = it }
                    )
                }
            }
        }
    }
}

@Composable
private fun TopBar() {
    Row(
        Modifier
            .fillMaxWidth()
            .background(Color(0xFF0D0F15))
            .padding(horizontal = 18.dp, vertical = 13.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier
                .size(44.dp)
                .background(
                    Brush.linearGradient(listOf(Crimson, Pink)),
                    RoundedCornerShape(14.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text("R", fontSize = 29.sp, fontWeight = FontWeight.Black, color = Ink)
        }

        Spacer(Modifier.width(11.dp))

        Column {
            Text("REDPLAY", fontSize = 21.sp, fontWeight = FontWeight.Black)
            Text(
                "GAMES • PLAY MORE. PLAY RED.",
                fontSize = 8.sp,
                color = Crimson
            )
        }

        Spacer(Modifier.weight(1f))
        Icon(
            Icons.Default.NotificationsNone,
            null,
            tint = White,
            modifier = Modifier.size(25.dp)
        )
    }
}

@Composable
private fun HomeScreen(
    favs: List<String>,
    open: (Game) -> Unit,
    toggle: (String) -> Unit,
    query: String,
    onQuery: (String) -> Unit,
    all: () -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(bottom = 100.dp)
    ) {
        item { Hero(all) }
        item { SearchField(query, onQuery) }
        item { SectionHeader("التصنيفات", "استكشف حسب النوع") }
        item {
            CategoryRail(
                listOf("كرة القدم", "أكشن", "سباق", "مغامرات", "أركيد"),
                all
            )
        }
        item { SectionHeader("الأكثر شهرة", "ألعاب يختارها اللاعبون") }
        item {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(catalog.take(6)) {
                    GamePoster(
                        it,
                        favs.contains(it.title),
                        { open(it) },
                        { toggle(it.title) }
                    )
                }
            }
        }
        item { SectionHeader("بدون إنترنت", "متعة في أي وقت") }
        item {
            Column(Modifier.padding(horizontal = 15.dp)) {
                catalog.filter { it.offline }.take(5).forEach {
                    GameRow(
                        it, favs.contains(it.title),
                        { open(it) }, { toggle(it.title) }
                    )
                }
            }
        }
    }
}

@Composable
private fun Hero(onExplore: () -> Unit) {
    val inf = rememberInfiniteTransition(label = "hero")
    val pulse by inf.animateFloat(
        .2f, .7f,
        infiniteRepeatable(tween(1800), RepeatMode.Reverse),
        label = "pulse"
    )

    Box(
        Modifier
            .fillMaxWidth()
            .padding(15.dp)
            .clip(RoundedCornerShape(26.dp))
            .background(
                Brush.linearGradient(
                    listOf(Color(0xFF5A0920), Color(0xFF24101B), Surface1)
                )
            )
            .border(1.dp, Crimson.copy(alpha = pulse), RoundedCornerShape(26.dp))
    ) {
        Column(Modifier.padding(22.dp)) {
            Text(
                "عالم الألعاب بين إيديك",
                color = Pink,
                fontSize = 11.sp
            )
            Spacer(Modifier.height(14.dp))
            Text(
                "العبها بطريقتك.",
                fontSize = 29.sp,
                fontWeight = FontWeight.Black
            )
            Text(
                "اكتشف. حمّل. استمتع.",
                fontSize = 23.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Pink
            )
            Spacer(Modifier.height(8.dp))
            Text(
                "وجهتك لاكتشاف ألعابك المفضلة في مكان واحد.",
                fontSize = 12.sp,
                color = Color(0xFFD1C7CD)
            )
            Spacer(Modifier.height(18.dp))
            Button(
                onClick = onExplore,
                colors = ButtonDefaults.buttonColors(containerColor = Crimson),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("استكشف المكتبة")
                Spacer(Modifier.width(7.dp))
                Icon(Icons.Default.ArrowBack, null)
            }
        }
    }
}

@Composable
private fun SearchField(value: String, onChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 5.dp),
        placeholder = {
            Text("ابحث عن لعبتك المفضلة...", color = Soft, fontSize = 12.sp)
        },
        leadingIcon = {
            Icon(Icons.Default.Search, null, tint = Crimson)
        },
        singleLine = true,
        shape = RoundedCornerShape(15.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Crimson,
            unfocusedBorderColor = Color(0xFF303440),
            focusedTextColor = White,
            unfocusedTextColor = White,
            cursorColor = Crimson,
            focusedContainerColor = Surface1,
            unfocusedContainerColor = Surface1
        )
    )
}

@Composable
private fun SectionHeader(title: String, sub: String) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(start = 18.dp, end = 18.dp, top = 15.dp, bottom = 9.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f)) {
            Text(title, fontSize = 17.sp, fontWeight = FontWeight.ExtraBold)
            Text(sub, fontSize = 10.sp, color = Soft)
        }
        Box(
            Modifier
                .width(4.dp)
                .height(28.dp)
                .background(Crimson, RoundedCornerShape(4.dp))
        )
    }
}

@Composable
private fun CategoryRail(items: List<String>, onPick: (String) -> Unit) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(9.dp)
    ) {
        items(items) { c ->
            Box(
                Modifier
                    .background(Surface2, RoundedCornerShape(13.dp))
                    .border(1.dp, Color(0xFF303440), RoundedCornerShape(13.dp))
                    .clickable { onPick(c) }
                    .padding(horizontal = 17.dp, vertical = 13.dp)
            ) {
                Text(c, color = White, fontSize = 12.sp)
            }
        }
    }
}

@Composable
private fun GamePoster(
    g: Game,
    fav: Boolean,
    open: () -> Unit,
    toggle: () -> Unit
) {
    Column(
        Modifier
            .width(145.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(Surface1)
            .clickable { open() }
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .height(115.dp)
                .background(
                    Brush.linearGradient(
                        listOf(Color(0xFF4A1023), Color(0xFF171923))
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(g.emoji, fontSize = 51.sp)
            Text(
                g.tag,
                Modifier
                    .align(Alignment.TopStart)
                    .padding(8.dp)
                    .background(Crimson, RoundedCornerShape(6.dp))
                    .padding(6.dp),
                fontSize = 8.sp
            )
            IconButton(
                onClick = toggle,
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Icon(
                    if (fav) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    null,
                    tint = if (fav) Crimson else White
                )
            }
        }
        Column(Modifier.padding(10.dp)) {
            Text(
                g.title,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(g.category, fontSize = 10.sp, color = Soft)
        }
    }
}

@Composable
private fun GameRow(
    g: Game,
    fav: Boolean,
    open: () -> Unit,
    toggle: () -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(Surface1)
            .clickable { open() }
            .padding(11.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier
                .size(52.dp)
                .background(Surface2, RoundedCornerShape(13.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(g.emoji, fontSize = 27.sp)
        }
        Spacer(Modifier.width(11.dp))
        Column(Modifier.weight(1f)) {
            Text(g.title, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            Text(
                "${g.category} • ${if (g.offline) "بدون إنترنت" else "إنترنت"}",
                fontSize = 10.sp,
                color = Soft
            )
        }
        IconButton(onClick = toggle) {
            Icon(
                if (fav) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                null,
                tint = if (fav) Crimson else Soft
            )
        }
        Icon(Icons.Default.ChevronLeft, null, tint = Soft)
    }
}

@Composable
private fun ListingScreen(
    list: List<Game>,
    cats: List<String>,
    cat: String,
    onCat: (String) -> Unit,
    query: String,
    onQuery: (String) -> Unit,
    favs: List<String>,
    open: (Game) -> Unit,
    toggle: (String) -> Unit,
    showFilter: Boolean = true
) {
    Column(Modifier.fillMaxSize()) {
        SectionHeader("مكتبة الألعاب", "اكتشف لعبتك التالية")
        SearchField(query, onQuery)

        if (showFilter) {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 14.dp),
                horizontalArrangement = Arrangement.spacedBy(7.dp)
            ) {
                items(cats) { c ->
                    FilterChip(
                        selected = cat == c,
                        onClick = { onCat(c) },
                        label = { Text(c) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Crimson,
                            selectedLabelColor = White
                        )
                    )
                }
            }
        }

        LazyColumn(
            contentPadding = PaddingValues(
                start = 14.dp,
                end = 14.dp,
                bottom = 100.dp
            )
        ) {
            items(list) {
                GameRow(
                    it,
                    favs.contains(it.title),
                    { open(it) },
                    { toggle(it.title) }
                )
            }
        }
    }
}

@Composable
private fun CategoriesScreen(
    cats: List<String>,
    pick: (String) -> Unit
) {
    Column(Modifier.fillMaxSize()) {
        SectionHeader("التصنيفات", "اختر عالمك المفضل")
        LazyColumn(contentPadding = PaddingValues(15.dp)) {
            items(cats) { c ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp)
                        .background(Surface1, RoundedCornerShape(15.dp))
                        .clickable { pick(c) }
                        .padding(17.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("🎮", fontSize = 24.sp)
                    Spacer(Modifier.width(13.dp))
                    Text(c, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                    Icon(Icons.Default.ChevronLeft, null, tint = Crimson)
                }
            }
        }
    }
}

@Composable
private fun DetailScreen(
    g: Game,
    fav: Boolean,
    toggle: () -> Unit,
    back: () -> Unit
) {
    LazyColumn(contentPadding = PaddingValues(bottom = 100.dp)) {
        item {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(235.dp)
                    .background(
                        Brush.linearGradient(
                            listOf(Color(0xFF6B0B29), Color(0xFF171923), Ink)
                        )
                    )
            ) {
                Text(
                    "رجوع",
                    Modifier
                        .align(Alignment.TopStart)
                        .clickable { back() }
                        .padding(18.dp),
                    color = White
                )
                Text("🎮", Modifier.align(Alignment.Center), fontSize = 82.sp)
                Text(
                    "REDPLAY • GAME DETAILS",
                    Modifier
                        .align(Alignment.BottomStart)
                        .padding(20.dp),
                    fontSize = 10.sp,
                    color = Pink
                )
            }
        }

        item {
            Column(Modifier.padding(19.dp)) {
                Text(g.title, fontSize = 25.sp, fontWeight = FontWeight.Black)
                Text(g.category, color = Pink, fontSize = 12.sp)
                Spacer(Modifier.height(13.dp))
                Text(g.desc, color = Soft, fontSize = 13.sp)
                Spacer(Modifier.height(20.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    InfoPill(
                        if (g.offline) "قد تعمل دون إنترنت" else "تحتاج إنترنت",
                        "الاتصال"
                    )
                    InfoPill("Android", "المنصة")
                }

                Spacer(Modifier.height(20.dp))

                Button(
                    onClick = toggle,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(13.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (fav) Surface2 else Crimson
                    )
                ) {
                    Icon(
                        if (fav) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        null
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        if (fav) "إزالة من المفضلة" else "أضف إلى المفضلة"
                    )
                }

                Spacer(Modifier.height(20.dp))
                Text("التحميل الآمن", fontWeight = FontWeight.ExtraBold, fontSize = 17.sp)
                Text(
                    "سيُضاف رابط المتجر الرسمي بعد التحقق من مصدره. لا تستخدم روابط مجهولة.",
                    color = Soft,
                    fontSize = 11.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}

@Composable
private fun InfoPill(text: String, label: String) {
    Column(
        Modifier
            .background(Surface1, RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {
        Text(label, fontSize = 9.sp, color = Soft)
        Text(text, fontSize = 11.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun ProfileScreen() {
    Column(Modifier.fillMaxSize().padding(18.dp)) {
        SectionHeader("ملف اللاعب", "مساحتك داخل REDPLAY")

        Box(
            Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(listOf(Color(0xFF4A1023), Surface1)),
                    RoundedCornerShape(20.dp)
                )
                .padding(20.dp)
        ) {
            Column {
                Text("👾", fontSize = 40.sp)
                Text("لاعب REDPLAY", fontWeight = FontWeight.ExtraBold, fontSize = 19.sp)
                Text("وضع الزائر", color = Pink, fontSize = 11.sp)
            }
        }

        Spacer(Modifier.height(14.dp))

        listOf(
            "الإعدادات",
            "اللغة: العربية",
            "المظهر: داكن",
            "سياسة الخصوصية",
            "عن REDPLAY GAMES"
        ).forEach {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
                    .background(Surface1, RoundedCornerShape(13.dp))
                    .padding(16.dp)
            ) {
                Text(it, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
private fun BottomNav(
    current: String,
    modifier: Modifier = Modifier,
    select: (String) -> Unit
) {
    val entries = listOf(
        "الرئيسية" to Icons.Default.Home,
        "الألعاب" to Icons.Default.SportsEsports,
        "التصنيفات" to Icons.Default.GridView,
        "المفضلة" to Icons.Default.Favorite,
        "حسابي" to Icons.Default.Person
    )

    Row(
        modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .background(Color(0xF20F1118))
            .border(0.5.dp, Color(0xFF292D38))
            .padding(top = 9.dp, bottom = 7.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        entries.forEach { (name, icon) ->
            val active = current == name

            val navScale by animateFloatAsState(
                targetValue = if (active) 1.12f else 1f,
                animationSpec = spring(
                    dampingRatio = .55f,
                    stiffness = 500f
                ),
                label = "navScale"
            )

            Column(
                Modifier
                    .weight(1f)
                    .clickable { select(name) },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = name,
                    tint = if (active) Crimson else Soft,
                    modifier = Modifier
                        .size(21.dp)
                        .graphicsLayer {
                            scaleX = navScale
                            scaleY = navScale
                        }
                )

                Spacer(Modifier.height(3.dp))

                Text(
                    name,
                    color = if (active) Crimson else Soft,
                    fontSize = 9.sp,
                    fontWeight = if (active) FontWeight.Bold else FontWeight.Normal
                )

                Spacer(Modifier.height(4.dp))

                Box(
                    Modifier
                        .width(if (active) 18.dp else 4.dp)
                        .height(2.dp)
                        .background(
                            if (active) Crimson else Color.Transparent,
                            CircleShape
                        )
                )
            }
        }
    }
}
