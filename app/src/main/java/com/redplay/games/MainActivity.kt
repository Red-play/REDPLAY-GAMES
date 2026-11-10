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
            .border(.5.dp, Color(0xFF292D38))
            .padding(top = 9.dp, bottom = 7.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        entries.forEach { (name, icon) ->
            val active = current == name

            val navScale by animateFloatAsState(
                if (active) 1.12f else 1f,
                spring(
                    dampingRatio = .55f,
                    stiffness = 500f
                ),
                label = "navScale"
            )

            Column(
                Modifier.weight(1f).clickable {
                    select(name)
                },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    icon,
                    null,
                    tint = if (active) Crimson else Soft,
                    modifier = Modifier.size(21.dp).graphicsLayer {
                        scaleX = navScale
                        scaleY = navScale
                    }
                )

                Spacer(Modifier.height(3.dp))

                Text(
                    name,
                    color = if (active) Crimson else Soft,
                    fontSize = 9.sp,
                    fontWeight = if (active) FontWeight.Bold
                    else FontWeight.Normal
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
