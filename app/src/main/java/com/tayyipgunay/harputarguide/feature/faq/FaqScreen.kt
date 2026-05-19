package com.tayyipgunay.harputarguide.feature.faq

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tayyipgunay.harputarguide.R
import com.tayyipgunay.harputarguide.core.design.component.ChatMessageBubble
import com.tayyipgunay.harputarguide.core.design.component.GuideGreetingHeader
import com.tayyipgunay.harputarguide.core.design.component.GuideInputBar
import com.tayyipgunay.harputarguide.core.design.component.HarputBottomBar
import com.tayyipgunay.harputarguide.core.design.component.HarputBottomBarStyle
import com.tayyipgunay.harputarguide.core.design.component.HarputBottomTab
import com.tayyipgunay.harputarguide.core.design.component.SuggestedQuestionCard
import com.tayyipgunay.harputarguide.data.asset.FaqIconType
import com.tayyipgunay.harputarguide.data.asset.SampleFaqs
import kotlinx.coroutines.launch

private const val CHAT_TIMESTAMP = "10:34"
private const val GREETING =
    "Merhaba, sanal rehberinizim. Harput hakkında sorularınızı sorabilirsiniz."
private const val CUSTOM_REPLY =
    "Bu özellik sonraki sürümde canlı rehber desteğiyle geliştirilecektir. Şimdilik önerilen sorulardan birini seçebilirsiniz."

@Composable
fun FaqScreen(
    onBackClick: () -> Unit,
    onHomeClick: () -> Unit,
    onPlacesClick: () -> Unit,
    onVisitedClick: () -> Unit,
    onFavoritesClick: () -> Unit,
    onAboutClick: () -> Unit
) {
    val cream = Color(0xFFF5EBDD)
    val cardColor = Color(0xFFFDFBF7)
    val darkBrown = Color(0xFF4B2E1F)
    val softBrown = Color(0xFF72533C)
    val borderColor = Color(0xFFD8C9B6)
    val bottomBarBg = Color(0xFFF0E4D4)
    val guideBubble = Color(0xFFF8F1E6)

    var activeFaqId by remember { mutableStateOf(SampleFaqs.defaultEntryId) }
    var inputText by remember { mutableStateOf("") }
    var customQuestion by remember { mutableStateOf<String?>(null) }
    var customAnswer by remember { mutableStateOf<String?>(null) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val activeFaq = remember(activeFaqId) { SampleFaqs.findById(activeFaqId) }
    val displayQuestion = customQuestion ?: activeFaq?.question.orEmpty()
    val displayAnswer = customAnswer ?: activeFaq?.answer.orEmpty()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = cream,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            HarputBottomBar(
                selectedTab = HarputBottomTab.Places,
                style = HarputBottomBarStyle.Explore,
                onHomeClick = onHomeClick,
                onPlacesClick = onPlacesClick,
                onArClick = { },
                onVisitedClick = onVisitedClick,
                onFavoritesClick = onFavoritesClick,
                onAboutClick = onAboutClick,
                backgroundColor = bottomBarBg,
                activeColor = darkBrown,
                inactiveColor = softBrown,
                activeLabelColor = cream,
                inactiveLabelColor = softBrown
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            item {
                FaqTopBar(
                    locationName = SampleFaqs.locationName,
                    onBackClick = onBackClick,
                    onInfoClick = {
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                "Yanıtlar hazır bilgi tabanından alınır. Canlı AI sonraki sürümde eklenecektir."
                            )
                        }
                    },
                    darkBrown = darkBrown,
                    softBrown = softBrown
                )
            }

            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.harput_welcome_png),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 24.dp),
                        contentScale = ContentScale.Crop,
                        alpha = 0.12f
                    )
                    GuideGreetingHeader(
                        greeting = GREETING,
                        avatarColor = Color(0xFFD4C4AA),
                        avatarIconColor = darkBrown,
                        bubbleColor = cardColor,
                        textColor = darkBrown
                    )
                }
            }

            item {
                Text(
                    text = "Önerilen Sorular",
                    color = darkBrown,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }

            items(SampleFaqs.entries) { entry ->
                SuggestedQuestionCard(
                    question = entry.question,
                    icon = faqIcon(entry.iconType),
                    onClick = {
                        activeFaqId = entry.id
                        customQuestion = null
                        customAnswer = null
                        inputText = ""
                    },
                    cardColor = cardColor,
                    borderColor = borderColor,
                    textColor = darkBrown,
                    iconColor = darkBrown,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 5.dp)
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                FaqChatDivider(softBrown = softBrown)
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                ChatMessageBubble(
                    message = displayQuestion,
                    isUser = true,
                    timestamp = CHAT_TIMESTAMP,
                    userBubbleColor = darkBrown,
                    userTextColor = cream,
                    guideBubbleColor = guideBubble,
                    guideTextColor = darkBrown,
                    mutedColor = softBrown,
                    showGuideAvatar = false,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                )
            }

            item {
                ChatMessageBubble(
                    message = displayAnswer,
                    isUser = false,
                    timestamp = CHAT_TIMESTAMP,
                    userBubbleColor = darkBrown,
                    userTextColor = cream,
                    guideBubbleColor = guideBubble,
                    guideTextColor = darkBrown,
                    mutedColor = softBrown,
                    showGuideAvatar = true,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                )
            }

            item {
                GuideInputBar(
                    value = inputText,
                    onValueChange = { inputText = it },
                    onSendClick = {
                        val text = inputText.trim()
                        if (text.isNotEmpty()) {
                            customQuestion = text
                            customAnswer = CUSTOM_REPLY
                        }
                    },
                    placeholder = "Sorunuzu yazın...",
                    footerNote = "Yanıtlar hazır bilgi tabanından alınır.",
                    fieldColor = cardColor,
                    textColor = darkBrown,
                    mutedColor = softBrown,
                    sendButtonColor = darkBrown,
                    sendIconColor = cream,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
        }
    }
}

@Composable
private fun FaqTopBar(
    locationName: String,
    onBackClick: () -> Unit,
    onInfoClick: () -> Unit,
    darkBrown: Color,
    softBrown: Color
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 4.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Geri",
                    tint = darkBrown
                )
            }
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Rehbere Sor",
                    color = darkBrown,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    textAlign = TextAlign.Center
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.LocationOn,
                        contentDescription = null,
                        tint = softBrown,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = locationName,
                        color = softBrown,
                        fontSize = 13.sp
                    )
                }
            }
            IconButton(
                onClick = onInfoClick,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE8DFD0))
            ) {
                Icon(
                    imageVector = Icons.Filled.Info,
                    contentDescription = "Bilgi",
                    tint = darkBrown,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}

@Composable
private fun FaqChatDivider(softBrown: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            color = softBrown.copy(alpha = 0.3f)
        )
        Box(
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .size(6.dp)
                .clip(androidx.compose.foundation.shape.RoundedCornerShape(1.dp))
                .background(softBrown.copy(alpha = 0.5f))
        )
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            color = softBrown.copy(alpha = 0.3f)
        )
    }
}

private fun faqIcon(type: FaqIconType): ImageVector = when (type) {
    FaqIconType.Castle -> Icons.Filled.AccountBalance
    FaqIconType.Ticket -> Icons.Filled.ConfirmationNumber
    FaqIconType.Clock -> Icons.Filled.Schedule
    FaqIconType.Materials -> Icons.Filled.Apartment
}
