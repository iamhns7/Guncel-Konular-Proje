package com.tayyipgunay.harputarguide.feature.faq

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Science
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tayyipgunay.harputarguide.R
import com.tayyipgunay.harputarguide.core.design.theme.HarputColors
import com.tayyipgunay.harputarguide.core.design.component.ChatMessageBubble
import com.tayyipgunay.harputarguide.core.design.component.GuideGreetingHeader
import com.tayyipgunay.harputarguide.core.design.component.GuideInputBar
import com.tayyipgunay.harputarguide.core.design.component.HarputBottomBar
import com.tayyipgunay.harputarguide.core.design.component.HarputBottomBarStyle
import com.tayyipgunay.harputarguide.core.design.component.SuggestedQuestionCard
import com.tayyipgunay.harputarguide.data.asset.FaqEntry
import com.tayyipgunay.harputarguide.data.asset.FaqIconType
import com.tayyipgunay.harputarguide.data.asset.SampleFaqs
import kotlinx.coroutines.launch

private const val CHAT_TIMESTAMP = "10:34"

@Composable
fun FaqScreen(
    onBackClick: () -> Unit,
    onHomeClick: () -> Unit,
    onPlacesClick: () -> Unit,
    onVisitedClick: () -> Unit,
    onFavoritesClick: () -> Unit,
    onAboutClick: () -> Unit
) {
    val cream = HarputColors.Cream
    val cardColor = HarputColors.CardLight
    val darkBrown = HarputColors.DarkBrown
    val softBrown = HarputColors.SoftBrown
    val borderColor = HarputColors.Divider
    val bottomBarBg = HarputColors.BottomBarBg
    val guideBubble = HarputColors.CardCream

    var activeFaqId by remember { mutableStateOf(SampleFaqs.defaultEntryId) }
    var inputText by remember { mutableStateOf("") }
    var customQuestion by remember { mutableStateOf<String?>(null) }
    var customAnswer by remember { mutableStateOf<String?>(null) }
    var moreQuestionsExpanded by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val greeting = stringResource(R.string.faq_greeting)
    val customReply = stringResource(R.string.faq_custom_reply)
    val faqInfo = stringResource(R.string.faq_info)

    val activeFaq = remember(activeFaqId) { SampleFaqs.findById(activeFaqId) }
    val displayQuestion = customQuestion ?: activeFaq?.question.orEmpty()
    val displayAnswer = customAnswer ?: activeFaq?.answer.orEmpty()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = cream,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            HarputBottomBar(
                selectedTab = null,
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
                            snackbarHostState.showSnackbar(faqInfo)
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
                        greeting = greeting,
                        avatarColor = Color(0xFFD4C4AA),
                        avatarIconColor = darkBrown,
                        bubbleColor = cardColor,
                        textColor = darkBrown
                    )
                }
            }

            item {
                Text(
                    text = stringResource(R.string.faq_suggested_title),
                    color = darkBrown,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }

            items(SampleFaqs.featuredEntries) { entry ->
                FaqQuestionCard(
                    entry = entry,
                    cardColor = cardColor,
                    borderColor = borderColor,
                    darkBrown = darkBrown,
                    onSelect = {
                        activeFaqId = entry.id
                        customQuestion = null
                        customAnswer = null
                        inputText = ""
                    }
                )
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(R.string.faq_more_title),
                        color = darkBrown,
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Text(
                        text = stringResource(
                            if (moreQuestionsExpanded) R.string.faq_more_collapse
                            else R.string.faq_more_expand
                        ),
                        color = softBrown,
                        fontSize = 13.sp,
                        modifier = Modifier.clickable { moreQuestionsExpanded = !moreQuestionsExpanded }
                    )
                }
            }

            if (moreQuestionsExpanded) {
                items(SampleFaqs.moreEntries) { entry ->
                    FaqQuestionCard(
                        entry = entry,
                        cardColor = cardColor,
                        borderColor = borderColor,
                        darkBrown = darkBrown,
                        onSelect = {
                            activeFaqId = entry.id
                            customQuestion = null
                            customAnswer = null
                            inputText = ""
                        }
                    )
                }
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
                            customAnswer = customReply
                        }
                    },
                    placeholder = stringResource(R.string.faq_input_placeholder),
                    footerNote = stringResource(R.string.faq_input_footer),
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
                    contentDescription = stringResource(R.string.cd_back),
                    tint = darkBrown
                )
            }
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.faq_title),
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
                    .background(HarputColors.IconBg)
            ) {
                Icon(
                    imageVector = Icons.Filled.Info,
                    contentDescription = stringResource(R.string.cd_info),
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

@Composable
private fun FaqQuestionCard(
    entry: FaqEntry,
    cardColor: Color,
    borderColor: Color,
    darkBrown: Color,
    onSelect: () -> Unit
) {
    SuggestedQuestionCard(
        question = entry.question,
        icon = faqIcon(entry.iconType),
        onClick = onSelect,
        cardColor = cardColor,
        borderColor = borderColor,
        textColor = darkBrown,
        iconColor = darkBrown,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 5.dp)
    )
}

private fun faqIcon(type: FaqIconType): ImageVector = when (type) {
    FaqIconType.Castle -> Icons.Filled.AccountBalance
    FaqIconType.Ticket -> Icons.Filled.ConfirmationNumber
    FaqIconType.Clock -> Icons.Filled.Schedule
    FaqIconType.Materials -> Icons.Filled.Apartment
    FaqIconType.History -> Icons.Filled.AccountBalance
    FaqIconType.Excavation -> Icons.Filled.Science
    FaqIconType.Location -> Icons.Filled.LocationOn
    FaqIconType.Legend -> Icons.Filled.AutoStories
    FaqIconType.Info -> Icons.Filled.Info
}
