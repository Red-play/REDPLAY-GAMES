package com.redplay.games

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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

private val Ink=Color(0xFF08090D)
private val Surface1=Color(0xFF12141B)
private val Surface2=Color(0xFF1A1D27)
private val Crimson=Color(0xFFFF1744)
private val Pink=Color(0xFFFF5277)
private val Soft=Color(0xFF9DA3B4)
private val White=Color(0xFFF7F7FA)

data class Game(val title:String,val category:String,val desc:String,val emoji:String,val offline:Boolean,val tag:String)
private val catalog=listOf(
 Game("Dream League Soccer","كرة القدم","ابنِ ناديك، طوّر فريقك، ونافس في عالم كرة القدم.","⚽",false,"الأكثر شهرة"),
 Game("eFootball","كرة القدم","تجربة كرة قدم تنافسية مع أندية ونجوم عالميين.","🏟️",false,"كرة القدم"),
 Game("EA SPORTS FC Mobile","كرة القدم","كوّن فريق أحلامك وخض مباريات حماسية.","🏆",false,"مميز"),
 Game("PUBG Mobile","أكشن","قاتل للبقاء وكن آخر لاعب أو فريق صامد.","🎯",false,"ترند"),
 Game("Free Fire","أكشن","معارك بقاء سريعة ومليئة بالإثارة.","🔥",false,"ترند"),
 Game("Call of Duty Mobile","أكشن","أطوار قتال وخرائط متعددة في تجربة تصويب.","💥",false,"أكشن"),
 Game("Shadow Fight 2","قتال","واجه خصومًا أقوياء بأسلوب قتالي مميز.","🥷",true,"بدون نت"),
 Game("Minecraft","مغامرات","استكشف وابنِ عوالمك بإبداع لا محدود.","⛏️",true,"مغامرات"),
 Game("Roblox","مغامرات","اكتشف تجارب متنوعة من صنع مجتمع اللاعبين.","🧩",false,"مغامرات"),
 Game("GTA: San Andreas","مغامرات","قصة عالم مفتوح ومهام مليئة بالمغامرة.","🚘",true,"كلاسيكية"),
 Game("Asphalt Legends","سباق","سيارات خارقة وسباقات سريعة على حلبات مذهلة.","🏎️",false,"سباق"),
 Game("Hill Climb Racing","سباق","تسلق التلال وتجاوز العقبات واجمع المكافآت.","🚙",true,"بدون نت"),
 Game("Traffic Rider","سباق","قد دراجتك النارية وسط حركة المرور.","🏍️",true,"سباق"),
 Game("Subway Surfers","أركيد","اركض وتفادى القطارات واجمع العملات.","🚆",true,"مشهورة"),
 Game("Temple Run 2","أركيد","اهرب من المعبد واجمع الكنوز.","🏃",true,"أركيد"),
 Game("Plants vs. Zombies","استراتيجية","خطط لدفاعك وواجه موجات الزومبي.","🌻",true,"استراتيجية"),
 Game("Angry Birds 2","أركيد","استخدم مهارات الطيور لهدم الأبراج.","🐦",true,"أركيد"),
 Game("Stumble Guys","أركيد","تحدى اللاعبين في سباقات وعقبات مجنونة.","🤸",false,"مرح"),
 Game("Vector","أكشن","اركض بحرية ونفذ حركات باركور مذهلة.","🏃‍♂️",true,"أكشن"),
 Game("Candy Crush Saga","ألغاز","طابق الحلوى واجتز مئات المراحل.","🍬",true,"ألغاز")
)

class MainActivity:ComponentActivity(){override fun onCreate(savedInstanceState:Bundle?){super.onCreate(savedInstanceState);window.statusBarColor=android.graphics.Color.rgb(8,9,13);window.navigationBarColor=android.graphics.Color.rgb(8,9,13);setContent{RedplayApp()}}}

@Composable fun RedplayApp(){
 var tab by remember{mutableStateOf("الرئيسية")}
 var search by remember{mutableStateOf("")}
 var category by remember{mutableStateOf("الكل")}
 var selected by remember{mutableStateOf<Game?>(null)}
 val favorites= remember{mutableStateListOf<String>()}
 val cats=listOf("الكل","كرة القدم","أكشن","مغامرات","سباق","أركيد","قتال","ألغاز","استراتيجية")
 val filtered=catalog.filter{(category=="الكل"||it.category==category)&&(search.isBlank()||it.title.contains(search,true)||it.category.contains(search))}
 CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl){
  MaterialTheme(colorScheme=darkColorScheme(primary=Crimson,background=Ink,surface=Surface1,onBackground=White,onSurface=White)){
   Box(Modifier.fillMaxSize().background(Ink)){
    Column(Modifier.fillMaxSize()){
     TopBar()
     AnimatedContent(targetState=selected,transitionSpec={fadeIn(tween(220))+slideInHorizontally{it/12} togetherWith fadeOut(tween(140))},label="screen"){
      game-> if(game!=null) DetailScreen(game,favorites.contains(game.title),{if(favorites.contains(game.title))favorites.remove(game.title)else favorites.add(game.title)},{selected=null})
      else when(tab){
       "الألعاب"->ListingScreen(filtered,cats,category,{category=it},search,{search=it},favorites,{selected=it},{if(favorites.contains(it))favorites.remove(it)else favorites.add(it)})
       "التصنيفات"->CategoriesScreen(cats.drop(1)){category=it;tab="الألعاب"}
       "المفضلة"->ListingScreen(catalog.filter{favorites.contains(it.title)},cats,category,{category=it},search,{search=it},favorites,{selected=it},{if(favorites.contains(it))favorites.remove(it)else favorites.add(it)},false)
       "حسابي"->ProfileScreen()
       else->HomeScreen(favorites,{selected=it},{if(favorites.contains(it))favorites.remove(it)else favorites.add(it)},search,{search=it},{tab="الألعاب"})
      }
     }
    }
    if(selected==null)BottomNav(tab,{tab=it})
   }
  }
 }
}

@Composable private fun TopBar(){
 Row(Modifier.fillMaxWidth().background(Color(0xFF0D0F15)).padding(horizontal=18.dp,vertical=13.dp),verticalAlignment=Alignment.CenterVertically){
 Box(Modifier.size(44.dp).background(Brush.linearGradient(listOf(Crimson,Pink)),RoundedCornerShape(14.dp)),contentAlignment=Alignment.Center){Text("R",fontSize=29.sp,fontWeight=FontWeight.Black,color=Ink)}
 Spacer(Modifier.width(11.dp))
 Column {Text("REDPLAY",fontSize=21.sp,fontWeight=FontWeight.Black,letterSpacing=1.2.sp);Text("GAMES  •  PLAY MORE. PLAY RED.",fontSize=8.sp,color=Crimson,letterSpacing=1.1.sp)}
 Spacer(Modifier.weight(1f))
 Box(Modifier.size(39.dp).border(1.dp,Color(0xFF303440),CircleShape).clickable{}.padding(8.dp),contentAlignment=Alignment.Center){Icon(Icons.Default.NotificationsNone,null,tint=White)}
}
}

@Composable private fun HomeScreen(favs:List<String>,open:(Game)->Unit,toggle:(String)->Unit,query:String,onQuery:(String)->Unit,all:()->Unit){
 LazyColumn(contentPadding=PaddingValues(bottom=100.dp),verticalArrangement=Arrangement.spacedBy(4.dp)){
  item{Hero(all)}
  item{SearchField(query,onQuery)}
  item{SectionHeader("التصنيفات","استكشف حسب النوع")}
  item{CategoryRail(listOf("كرة القدم","أكشن","سباق","مغامرات","أركيد")){all()}}
  item{SectionHeader("الأكثر شهرة","ألعاب يختارها اللاعبون")}
  item{LazyRow(contentPadding=PaddingValues(horizontal=16.dp),horizontalArrangement=Arrangement.spacedBy(12.dp)){items(catalog.take(6)){GamePoster(it,favs.contains(it.title),{open(it)},{toggle(it.title)})}}}
  item{SectionHeader("بدون إنترنت","متعة في أي وقت")}
  item{Column(Modifier.padding(horizontal=15.dp)){catalog.filter{it.offline}.take(4).forEach{GameRow(it,favs.contains(it.title),{open(it)},{toggle(it.title)})}}}
  item{SectionHeader("اكتشف المزيد","مكتبة REDPLAY")}
  item{Column(Modifier.padding(horizontal=15.dp)){catalog.drop(6).take(8).forEach{GameRow(it,favs.contains(it.title),{open(it)},{toggle(it.title)})}}}
 }
}

@Composable private fun Hero(onExplore:()->Unit){
 val inf= rememberInfiniteTransition(label="glow")
 val pulse by inf.animateFloat(0.22f,0.72f,infiniteRepeatable(tween(1800,easing=FastOutSlowInEasing),RepeatMode.Reverse),label="pulse")
 Box(Modifier.fillMaxWidth().padding(15.dp).clip(RoundedCornerShape(26.dp)).background(Brush.linearGradient(listOf(Color(0xFF5A0920),Color(0xFF24101B),Color(0xFF12141B)))).border(1.dp,Crimson.copy(alpha=pulse),RoundedCornerShape(26.dp))){
  val sweep by inf.animateFloat(-260f, 520f, infiniteRepeatable(tween(3600, easing=LinearEasing), RepeatMode.Restart), label="heroSweep")
  val orb by inf.animateFloat(0f, 1f, infiniteRepeatable(tween(2400, easing=FastOutSlowInEasing), RepeatMode.Reverse), label="heroOrb")
  Box(Modifier.size(180.dp).align(Alignment.TopEnd).offset(x=55.dp,y=(-45).dp).background(Crimson.copy(alpha=.12f + .08f*orb),CircleShape).blur(25.dp))
  Box(Modifier.fillMaxSize().graphicsLayer { translationX = sweep }.background(Brush.horizontalGradient(listOf(Color.Transparent, Color.White.copy(alpha=.045f), Color.Transparent))))
  Column(Modifier.padding(22.dp)){
   Row(verticalAlignment=Alignment.CenterVertically){Box(Modifier.background(Crimson.copy(alpha=.18f),RoundedCornerShape(30.dp)).padding(horizontal=10.dp,vertical=5.dp)){Text("عالم الألعاب بين إيديك",color=Pink,fontSize=10.sp,fontWeight=FontWeight.Bold)}}
   Spacer(Modifier.height(14.dp))
   Text("العبها بطريقتك.",fontSize=29.sp,fontWeight=FontWeight.Black,color=White)
   Text("اكتشف. حمّل. استمتع.",fontSize=23.sp,fontWeight=FontWeight.ExtraBold,color=Pink)
   Spacer(Modifier.height(8.dp))
   Text("وجهتك لاكتشاف ألعابك المفضلة في مكان واحد.",fontSize=12.sp,color=Color(0xFFD1C7CD))
   Spacer(Modifier.height(18.dp))
   Row(verticalAlignment=Alignment.CenterVertically,modifier=Modifier.clickable{onExplore()}.background(Brush.horizontalGradient(listOf(Crimson,Pink)),RoundedCornerShape(12.dp)).padding(horizontal=17.dp,vertical=11.dp)){
    Text("استكشف المكتبة",fontWeight=FontWeight.Bold,color=White,fontSize=12.sp);Spacer(Modifier.width(8.dp));Icon(Icons.Default.ArrowBack,null,tint=White,modifier=Modifier.size(16.dp))
   }
  }
 }
}

@Composable private fun SearchField(value:String,onChange:(String)->Unit){
 OutlinedTextField(value,onChange,Modifier.fillMaxWidth().padding(horizontal=16.dp,vertical=5.dp),placeholder={Text("ابحث عن لعبتك المفضلة...",color=Soft,fontSize=12.sp)},leadingIcon={Icon(Icons.Default.Search,null,tint=Crimson)},singleLine=true,shape=RoundedCornerShape(15.dp),colors=OutlinedTextFieldDefaults.colors(focusedBorderColor=Crimson,unfocusedBorderColor=Color(0xFF303440),focusedTextColor=White,unfocusedTextColor=White,cursorColor=Crimson,focusedContainerColor=Surface1,unfocusedContainerColor=Surface1))
}
@Composable private fun SectionHeader(title:String,sub:String){
 Row(Modifier.fillMaxWidth().padding(start=18.dp,end=18.dp,top=15.dp,bottom=9.dp),verticalAlignment=Alignment.CenterVertically){
 Column(Modifier.weight(1f)){Text(title,fontSize=17.sp,fontWeight=FontWeight.ExtraBold);Text(sub,fontSize=10.sp,color=Soft)}
 Box(Modifier.width(4.dp).height(28.dp).background(Crimson,RoundedCornerShape(4.dp)))
 }
}
@Composable private fun CategoryRail(items:List<String>,onPick:(String)->Unit){
 LazyRow(contentPadding=PaddingValues(horizontal=16.dp),horizontalArrangement=Arrangement.spacedBy(9.dp)){items(items){c->Box(Modifier.background(Surface2,RoundedCornerShape(13.dp)).border(1.dp,Color(0xFF303440),RoundedCornerShape(13.dp)).clickable{onPick(c)}.padding(horizontal=17.dp,vertical=13.dp)){Text(c,color=White,fontSize=12.sp,fontWeight=FontWeight.SemiBold)}}}
}
@Composable private fun GamePoster(g:Game,fav:Boolean,open:()->Unit,toggle:()->Unit){
 var appeared by remember { mutableStateOf(false) }
 LaunchedEffect(Unit) { appeared = true }
 val reveal by animateFloatAsState(if(appeared) 1f else .72f, spring(dampingRatio=.72f, stiffness=260f), label="posterReveal")
 Column(Modifier.width(145.dp).graphicsLayer { alpha = reveal; scaleX = reveal; scaleY = reveal }.clip(RoundedCornerShape(18.dp)).background(Surface1).clickable{open()}){
  Box(Modifier.fillMaxWidth().height(115.dp).background(Brush.linearGradient(listOf(Color(0xFF4A1023),Color(0xFF171923)))),contentAlignment=Alignment.Center){
   Text(g.emoji,fontSize=51.sp)
   Box(Modifier.align(Alignment.TopStart).padding(8.dp).background(Crimson,RoundedCornerShape(6.dp)).padding(horizontal=7.dp,vertical=4.dp)){Text(g.tag,fontSize=8.sp,fontWeight=FontWeight.Bold,color=White)}
   IconButton(onClick=toggle,modifier=Modifier.align(Alignment.TopEnd).size(34.dp)){Icon(if(fav)Icons.Default.Favorite else Icons.Default.FavoriteBorder,null,tint=if(fav)Crimson else White,modifier=Modifier.size(18.dp))}
  }
  Column(Modifier.padding(10.dp)){Text(g.title,fontSize=12.sp,fontWeight=FontWeight.Bold,maxLines=1,overflow=TextOverflow.Ellipsis);Text(g.category,fontSize=10.sp,color=Soft)}
 }
}
@Composable private fun GameRow(g:Game,fav:Boolean,open:()->Unit,toggle:()->Unit){
 var appeared by remember { mutableStateOf(false) }
 LaunchedEffect(Unit) { appeared = true }
 val reveal by animateFloatAsState(if(appeared) 1f else .0f, tween(360, easing=FastOutSlowInEasing), label="rowReveal")
 Row(Modifier.fillMaxWidth().graphicsLayer { alpha = reveal; translationY = (1f-reveal)*18f }.padding(vertical=5.dp).clip(RoundedCornerShape(15.dp)).background(Surface1).clickable{open()}.padding(11.dp),verticalAlignment=Alignment.CenterVertically){
  Box(Modifier.size(52.dp).background(Brush.linearGradient(listOf(Color(0xFF4A1023),Surface2)),RoundedCornerShape(13.dp)),contentAlignment=Alignment.Center){Text(g.emoji,fontSize=27.sp)}
  Spacer(Modifier.width(11.dp));Column(Modifier.weight(1f)){Text(g.title,fontWeight=FontWeight.Bold,fontSize=12.sp,maxLines=1);Text("${g.category}  •  ${if(g.offline)"بدون إنترنت" else "إنترنت"}",fontSize=10.sp,color=Soft)}
  IconButton(onClick=toggle){Icon(if(fav)Icons.Default.Favorite else Icons.Default.FavoriteBorder,null,tint=if(fav)Crimson else Soft)}
  Icon(Icons.Default.ChevronLeft,null,tint=Soft)
 }
}
@Composable private fun ListingScreen(list:List<Game>,cats:List<String>,cat:String,onCat:(String)->Unit,query:String,onQuery:(String)->Unit,favs:List<String>,open:(Game)->Unit,toggle:(String)->Unit,showFilter:Boolean=true){
 Column(Modifier.fillMaxSize()){
  SectionHeader("مكتبة الألعاب","اكتشف لعبتك التالية")
  SearchField(query,onQuery)
  if(showFilter)LazyRow(contentPadding=PaddingValues(horizontal=14.dp),horizontalArrangement=Arrangement.spacedBy(7.dp)){items(cats){c->FilterChip(selected=cat==c,onClick={onCat(c)},label={Text(c)},colors=FilterChipDefaults.filterChipColors(selectedContainerColor=Crimson,selectedLabelColor=White))}}
  LazyColumn(contentPadding=PaddingValues(start=14.dp,end=14.dp,bottom=100.dp)){items(list){g->GameRow(g,favs.contains(g.title),{open(g)},{toggle(g.title)})}}
 }
}
@Composable private fun CategoriesScreen(cats:List<String>,pick:(String)->Unit){
 Column(Modifier.fillMaxSize()){SectionHeader("التصنيفات","اختر عالمك المفضل");LazyColumn(contentPadding=PaddingValues(15.dp)){items(cats){c->Row(Modifier.fillMaxWidth().padding(vertical=5.dp).background(Surface1,RoundedCornerShape(15.dp)).clickable{pick(c)}.padding(17.dp),verticalAlignment=Alignment.CenterVertically){Text("🎮",fontSize=24.sp);Spacer(Modifier.width(13.dp));Text(c,fontWeight=FontWeight.Bold,modifier=Modifier.weight(1f));Icon(Icons.Default.ChevronLeft,null,tint=Crimson)}}}}
}
@Composable private fun DetailScreen(g:Game,fav:Boolean,toggle:()->Unit,back:()->Unit){
 val detailMotion = rememberInfiniteTransition(label="detailMotion")
 val halo by detailMotion.animateFloat(.10f,.30f,infiniteRepeatable(tween(1500),RepeatMode.Reverse),label="detailHalo")
 LazyColumn(contentPadding=PaddingValues(bottom=100.dp)){
  item{Box(Modifier.fillMaxWidth().height(235.dp).background(Brush.linearGradient(listOf(Color(0xFF6B0B29),Color(0xFF171923),Ink)))){Box(Modifier.align(Alignment.Center).size(190.dp).background(Crimson.copy(alpha=halo),CircleShape).blur(34.dp));Text("رجوع",Modifier.align(Alignment.TopStart).clickable{back()}.padding(18.dp),color=White);Text(g.emoji,Modifier.align(Alignment.Center),fontSize=82.sp);Text("REDPLAY • GAME DETAILS",Modifier.align(Alignment.BottomStart).padding(20.dp),fontSize=10.sp,color=Pink,fontWeight=FontWeight.Bold)}}
  item{Column(Modifier.padding(19.dp)){Text(g.title,fontSize=25.sp,fontWeight=FontWeight.Black);Text(g.category,color=Pink,fontSize=12.sp);Spacer(Modifier.height(13.dp));Text(g.desc,color=Soft,fontSize=13.sp);Spacer(Modifier.height(20.dp));Row(horizontalArrangement=Arrangement.spacedBy(8.dp)){InfoPill(if(g.offline)"قد تعمل دون إنترنت" else "تحتاج إنترنت","الاتصال");InfoPill("Android","المنصة")};Spacer(Modifier.height(20.dp));Button(onClick=toggle,Modifier.fillMaxWidth(),shape=RoundedCornerShape(13.dp),colors=ButtonDefaults.buttonColors(containerColor=if(fav)Surface2 else Crimson)){Icon(if(fav)Icons.Default.Favorite else Icons.Default.FavoriteBorder,null);Spacer(Modifier.width(8.dp));Text(if(fav)"إزالة من المفضلة" else "أضف إلى المفضلة",fontWeight=FontWeight.Bold)};Spacer(Modifier.height(20.dp));Text("التحميل الآمن",fontWeight=FontWeight.ExtraBold,fontSize=17.sp);Text("سيُضاف رابط المتجر الرسمي بعد التحقق من مصدره. لا تستخدم روابط مجهولة.",color=Soft,fontSize=11.sp,modifier=Modifier.padding(top=8.dp))}}
 }
}
@Composable private fun InfoPill(text:String,label:String){Column(Modifier.background(Surface1,RoundedCornerShape(12.dp)).padding(12.dp)){Text(label,fontSize=9.sp,color=Soft);Text(text,fontSize=11.sp,fontWeight=FontWeight.Bold)}}
@Composable private fun ProfileScreen(){Column(Modifier.fillMaxSize().padding(18.dp)){SectionHeader("ملف اللاعب","مساحتك داخل REDPLAY");Box(Modifier.fillMaxWidth().background(Brush.linearGradient(listOf(Color(0xFF4A1023),Surface1)),RoundedCornerShape(20.dp)).padding(20.dp)){Column{Text("👾",fontSize=40.sp);Text("لاعب REDPLAY",fontWeight=FontWeight.ExtraBold,fontSize=19.sp);Text("وضع الزائر",color=Pink,fontSize=11.sp)}};Spacer(Modifier.height(14.dp));listOf("الإعدادات","اللغة: العربية","المظهر: داكن","سياسة الخصوصية","عن REDPLAY GAMES").forEach{Row(Modifier.fillMaxWidth().padding(vertical=5.dp).background(Surface1,RoundedCornerShape(13.dp)).padding(16.dp)){Text(it,fontSize=13.sp,fontWeight=FontWeight.SemiBold)}}}}
@Composable private fun BottomNav(current:String,select:(String)->Unit){
 val entries=listOf("الرئيسية" to Icons.Default.Home,"الألعاب" to Icons.Default.SportsEsports,"التصنيفات" to Icons.Default.GridView,"المفضلة" to Icons.Default.Favorite,"حسابي" to Icons.Default.Person)
 Row(Modifier.align(Alignment.BottomCenter).fillMaxWidth().navigationBarsPadding().background(Color(0xF20F1118)).border(.5.dp,Color(0xFF292D38)).padding(top=9.dp,bottom=7.dp),horizontalArrangement=Arrangement.SpaceEvenly){
  entries.forEach{(name,icon)->val active=current==name;val navScale by animateFloatAsState(if(active)1.12f else 1f, spring(dampingRatio=.55f, stiffness=500f), label="navScale");Column(Modifier.weight(1f).clickable{select(name)},horizontalAlignment=Alignment.CenterHorizontally){Icon(icon,null,tint=if(active)Crimson else Soft,modifier=Modifier.size(21.dp).graphicsLayer{scaleX=navScale;scaleY=navScale});Spacer(Modifier.height(3.dp));Text(name,color=if(active)Crimson else Soft,fontSize=9.sp,fontWeight=if(active)FontWeight.Bold else FontWeight.Normal);Spacer(Modifier.height(4.dp));Box(Modifier.width(if(active)18.dp else 4.dp).height(2.dp).background(if(active)Crimson else Color.Transparent,CircleShape))}}
 }
}
