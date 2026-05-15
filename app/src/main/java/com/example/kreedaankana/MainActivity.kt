package com.example.kreedaankana

import android.os.Bundle
import androidx.activity.ComponentActivity
import android.app.DatePickerDialog
import androidx.compose.ui.layout.ContentScale

import com.google.firebase.database.DatabaseReference
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.activity.compose.setContent
import androidx.compose.ui.draw.paint
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.database.FirebaseDatabase

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            KreedaApp()
        }
    }
}

data class Slot(
    val time: String,
    var available: Boolean = true,
    var bookedBy: String = ""
)

@Composable
fun KreedaApp() {

    val db = FirebaseDatabase.getInstance().reference

    val context = LocalContext.current

    var username by remember { mutableStateOf("") }
    var isLoggedIn by remember { mutableStateOf(false) }

    var teamName by remember { mutableStateOf("") }

    var selectedSport by remember { mutableStateOf("Cricket") }

    var selectedDate by remember {
        mutableStateOf("Select Date")
    }

    val slots = remember {
        mutableStateListOf(
            Slot("6 AM - 7 AM"),
            Slot("7 AM - 8 AM"),
            Slot("8 AM - 9 AM"),
            Slot("4 PM - 5 PM"),
            Slot("5 PM - 6 PM"),
            Slot("6 PM - 7 PM"),
            Slot("7 PM - 8 PM"),
            Slot("8 PM - 9 PM")
        )
    }

    val bookedTeams = remember {
        mutableStateListOf<String>()
    }

    LaunchedEffect(selectedDate) {

        if (selectedDate != "Select Date") {

            db.child("bookings")
                .get()
                .addOnSuccessListener { snapshot ->

                    slots.forEach {
                        it.available = true
                        it.bookedBy = ""
                    }

                    bookedTeams.clear()

                    snapshot.children.forEach { data ->

                        val date =
                            data.child("date")
                                .value.toString()

                        if (date == selectedDate) {

                            val time =
                                data.child("time")
                                    .value.toString()

                            val team =
                                data.child("team")
                                    .value.toString()

                            val sport =
                                data.child("sport")
                                    .value.toString()

                            slots.forEach { slot ->

                                if (slot.time == time) {

                                    slot.available = false
                                    slot.bookedBy = team
                                }
                            }

                            bookedTeams.add(
                                "$team - $time - $sport"
                            )
                        }
                    }
                }
        }
    }

    if (!isLoggedIn) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .paint(
                    painterResource(id = R.drawable.sportsbg),
                    contentScale = ContentScale.Crop
                )
        ) {

            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xAA000000)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "🏆 Kreeda-Ankana",
                        color = Color.White,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Village Sports Organizer",
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    OutlinedTextField(
                        value = username,
                        onValueChange = {
                            username = it
                        },
                        label = {
                            Text(
                                "Enter Username",
                                color = Color.White
                            )
                        },
                        textStyle = LocalTextStyle.current.copy(
                            color = Color.White
                        )
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {

                            if (username.isNotEmpty()) {
                                isLoggedIn = true
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFF9800)
                        )
                    ) {

                        Text("LOGIN")
                    }
                }
            }
        }

    } else {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color(0xFF001F3F),
                            Color(0xFF003366),
                            Color.Black
                        )
                    )
                )
        ) {

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {

                item {

                    Text(
                        text = "Welcome $username 👋",
                        color = Color.White,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF0B8F5A)
                        ),
                        shape = RoundedCornerShape(20.dp)
                    ) {

                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {

                            Text(
                                text = "About Kreeda-Ankana",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = "Book grounds, organize matches, find opponents and build village sports culture.",
                                color = Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF146B2E)
                        ),
                        shape = RoundedCornerShape(24.dp)
                    ) {

                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {

                            Text(
                                text = "📅 Ground Calendar",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 28.sp
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            OutlinedTextField(
                                value = teamName,
                                onValueChange = {
                                    teamName = it
                                },
                                label = {
                                    Text(
                                        "Enter Team Name",
                                        color = Color.Black
                                    )
                                },
                                textStyle = LocalTextStyle.current.copy(
                                    color = Color.Black
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            Button(
                                onClick = {

                                    val calendar =
                                        java.util.Calendar.getInstance()

                                    DatePickerDialog(
                                        context,
                                        { _, year, month, day ->

                                            selectedDate =
                                                "$day/${month + 1}/$year"

                                        },
                                        calendar.get(
                                            java.util.Calendar.YEAR
                                        ),
                                        calendar.get(
                                            java.util.Calendar.MONTH
                                        ),
                                        calendar.get(
                                            java.util.Calendar.DAY_OF_MONTH
                                        )
                                    ).show()
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFFF9800)
                                )
                            ) {

                                Text(selectedDate)
                            }

                            Spacer(modifier = Modifier.height(18.dp))

                            Row(
                                horizontalArrangement =
                                    Arrangement.spacedBy(10.dp)
                            ) {

                                SportButton(
                                    "Cricket",
                                    selectedSport
                                ) {
                                    selectedSport = "Cricket"
                                }

                                SportButton(
                                    "Football",
                                    selectedSport
                                ) {
                                    selectedSport = "Football"
                                }

                                SportButton(
                                    "Volleyball",
                                    selectedSport
                                ) {
                                    selectedSport = "Volleyball"
                                }
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            Text(
                                text = "Available Slots",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 22.sp
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            LazyVerticalGrid(
                                columns = GridCells.Fixed(2),
                                modifier = Modifier.height(500.dp),
                                verticalArrangement =
                                    Arrangement.spacedBy(12.dp),
                                horizontalArrangement =
                                    Arrangement.spacedBy(12.dp)
                            ) {

                                items(slots) { slot ->

                                    Card(
                                        colors = CardDefaults.cardColors(
                                            containerColor =
                                                if (slot.available)
                                                    Color(0xFF42A5F5)
                                                else
                                                    Color.Red
                                        ),
                                        shape = RoundedCornerShape(20.dp)
                                    ) {

                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(16.dp),
                                            horizontalAlignment =
                                                Alignment.CenterHorizontally
                                        ) {

                                            Text(
                                                text = slot.time,
                                                color = Color.White,
                                                fontWeight = FontWeight.Bold
                                            )

                                            Spacer(
                                                modifier = Modifier.height(8.dp)
                                            )

                                            Text(
                                                text =
                                                    if (slot.available)
                                                        "AVAILABLE"
                                                    else
                                                        slot.bookedBy,
                                                color = Color.White
                                            )

                                            Spacer(
                                                modifier = Modifier.height(10.dp)
                                            )

                                            Button(
                                                onClick = {

                                                    if (
                                                        slot.available &&
                                                        teamName.isNotEmpty() &&
                                                        selectedDate != "Select Date"
                                                    ) {

                                                        slot.available = false
                                                        slot.bookedBy = teamName

                                                        bookedTeams.add(
                                                            "$teamName - ${slot.time} - $selectedSport"
                                                        )

                                                        db.child("bookings")
                                                            .push()
                                                            .setValue(
                                                                mapOf(
                                                                    "team" to teamName,
                                                                    "sport" to selectedSport,
                                                                    "time" to slot.time,
                                                                    "date" to selectedDate
                                                                )
                                                            )
                                                    }
                                                },
                                                colors =
                                                    ButtonDefaults.buttonColors(
                                                        containerColor =
                                                            Color(0xFFFF9800)
                                                    )
                                            ) {

                                                Text("BOOK")
                                            }
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = Color(0xFF00695C)
                                ),
                                shape = RoundedCornerShape(20.dp)
                            ) {

                                Column(
                                    modifier = Modifier.padding(16.dp)
                                ) {

                                    Text(
                                        text = "✔ Booked Teams",
                                        color = Color.White,
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Bold
                                    )

                                    Spacer(
                                        modifier = Modifier.height(14.dp)
                                    )

                                    bookedTeams.forEach {

                                        Card(
                                            colors =
                                                CardDefaults.cardColors(
                                                    containerColor =
                                                        Color(0xFF00897B)
                                                ),
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 6.dp)
                                        ) {

                                            Text(
                                                text = it,
                                                color = Color.White,
                                                modifier =
                                                    Modifier.padding(14.dp)
                                            )
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            ChallengeBoard()

                            Spacer(modifier = Modifier.height(24.dp))

                            ScoreWall()
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun SportButton(
    sport: String,
    selectedSport: String,
    onClick: () -> Unit
) {

    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor =
                if (selectedSport == sport)
                    Color(0xFFFF9800)
                else
                    Color.DarkGray
        )
    ) {

        Text(
            text = sport,
            color = Color.White
        )
    }
}

@Composable
fun ChallengeBoard() {
    val db = FirebaseDatabase.getInstance().reference
    var challengeTeam by remember { mutableStateOf("") }
    var challengeSport by remember { mutableStateOf("") }
    var challengeTime by remember { mutableStateOf("") }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF7B1FA2)
        ),
        shape = RoundedCornerShape(20.dp)
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = "⚡ Challenge Board",
                color = Color.White,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(14.dp))

            OutlinedTextField(
                value = challengeTeam,
                onValueChange = { challengeTeam = it },

                label = {
                    Text("Enter Team", color = Color.White)
                },

                textStyle = LocalTextStyle.current.copy(
                    color = Color.White
                ),

                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = challengeSport,
                onValueChange = { challengeSport = it },

                label = {
                    Text("Enter Sport", color = Color.White)
                },

                textStyle = LocalTextStyle.current.copy(
                    color = Color.White
                ),

                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = challengeTime,

                onValueChange = {
                    challengeTime = it
                },

                label = {
                    Text(
                        "Enter Time",
                        color = Color.White
                    )
                },

                textStyle = LocalTextStyle.current.copy(
                    color = Color.White
                ),

                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {

                    val challenge = mapOf(
                        "team" to challengeTeam,
                        "game" to challengeSport,
                        "day" to "Sunday",
                        "time" to challengeTime,
                        "replies" to 0
                    )

                    db.child("challenges")
                        .push()
                        .setValue(challenge)

                    challengeTeam = ""
                    challengeSport = ""
                    challengeTime = ""

                },

                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFF9800)
                )

            ) {
                Text(
                    "POST CHALLENGE",
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            ChallengeCard("Mandya Tigers", "Volleyball", "Sunday", "5 PM")
            ChallengeCard("Mysuru Kings", "Cricket", "Saturday", "4 PM")
            ChallengeCard("Kodagu Warriors", "Football", "Friday", "6 PM")
        }
    }
}

@Composable
fun ChallengeCard(
    team: String,
    game: String,
    day: String,
    time: String
) {

    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF9C27B0)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = team,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Game: $game",
                color = Color.Yellow
            )

            Text(
                text = "Day: $day",
                color = Color.White
            )

            Text(
                text = "Time: $time",
                color = Color.White
            )

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFF9800)
                )
            ) {

                Text("REPLY")
            }
        }
    }
}

@Composable
fun ScoreWall() {
    val db = FirebaseDatabase.getInstance().reference
    var matchName by remember { mutableStateOf("") }
    var matchResult by remember { mutableStateOf("") }
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFD32F2F)
        ),
        shape = RoundedCornerShape(20.dp)
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = "🏅 Score Wall",
                color = Color.White,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(14.dp))

            OutlinedTextField(
                value = matchName,
                onValueChange = { matchName = it },

                label = {
                    Text("Match", color = Color.White)
                },

                textStyle = LocalTextStyle.current.copy(
                    color = Color.White
                ),

                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = matchResult,
                onValueChange = { matchResult = it },

                label = {
                    Text("Result", color = Color.White)
                },

                textStyle = LocalTextStyle.current.copy(
                    color = Color.White
                ),

                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = {

                    val score = mapOf(
                        "match" to matchName,
                        "result" to matchResult
                    )

                    db.child("scores")
                        .push()
                        .setValue(score)

                    matchName = ""
                    matchResult = ""

                },

                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFF9800)
                )

            ) {
                Text(
                    "POST SCORE",
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            ScoreCard(
                "Cricket",
                "Mandya Tigers vs Mysuru Kings",
                "145/6 vs 132/8",
                "Mandya Tigers Won"
            )

            ScoreCard(
                "Football",
                "Kodagu Warriors vs Hassan Strikers",
                "3 - 1",
                "Kodagu Warriors Won"
            )

            ScoreCard(
                "Volleyball",
                "Village Smashers vs Rural Spikers",
                "3 Sets - 2 Sets",
                "Village Smashers Won"
            )
        }
    }
}

@Composable
fun ScoreCard(
    game: String,
    teams: String,
    score: String,
    result: String
) {

    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFF5252)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {

        Column(
            modifier = Modifier.padding(14.dp)
        ) {

            Text(
                text = "Game: $game",
                color = Color.Yellow,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = teams,
                color = Color.White
            )

            Text(
                text = "Score: $score",
                color = Color.White
            )

            Text(
                text = result,
                color = Color.Green,
                fontWeight = FontWeight.Bold
            )
        }
    }
}