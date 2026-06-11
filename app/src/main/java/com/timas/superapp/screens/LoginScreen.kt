package com.timas.superapp.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.TextStyle
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// ─── Renkler ────────────────────────────────────────────────
private val Orange     = Color(0xFFF26122)
private val DarkSlate  = Color(0xFF1E293B)
private val SlateLight = Color(0xFF334155)
private val BgColor    = Color(0xFFF8FAFC)
private val TextMain   = Color(0xFF0F172A)
private val TextMuted  = Color(0xFF64748B)
private val BorderClr  = Color(0xFFE2E8F0)

@Composable
fun LoginScreen(
    onBack: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    var step by remember { mutableStateOf(LoginStep.PHONE) }
    var phoneNumber by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current

    LaunchedEffect(Unit) {
        focusManager.clearFocus()
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(BgColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
        ) {
            // ── Üst bar ──
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    if (step == LoginStep.OTP) step = LoginStep.PHONE
                    else onBack()
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Geri",
                        tint = DarkSlate
                    )
                }
            }

            // ── İçerik ──
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                // Logo / başlık alanı
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(Orange, Color(0xFFFF8C42))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(36.dp)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                AnimatedContent(
                    targetState = step,
                    transitionSpec = {
                        slideInHorizontally { it } + fadeIn() togetherWith
                                slideOutHorizontally { -it } + fadeOut()
                    },
                    label = "step_transition"
                ) { currentStep ->
                    when (currentStep) {
                        LoginStep.PHONE -> PhoneStep(
                            phoneNumber = phoneNumber,
                            onPhoneChange = { phoneNumber = it },
                            isLoading = isLoading,
                            onContinue = {
                                scope.launch {
                                    isLoading = true
                                    delay(800) // simüle edilmiş SMS gönderimi
                                    isLoading = false
                                    step = LoginStep.OTP
                                }
                            }
                        )
                        LoginStep.OTP -> OtpStep(
                            phoneNumber = phoneNumber,
                            isLoading = isLoading,
                            onVerify = { otp ->
                                scope.launch {
                                    isLoading = true
                                    delay(800) // simüle edilmiş doğrulama
                                    isLoading = false
                                    onLoginSuccess()
                                }
                            },
                            onResend = {
                                scope.launch {
                                    isLoading = true
                                    delay(600)
                                    isLoading = false
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

// ─── Telefon Numarası Adımı ─────────────────────────────────
@Composable
private fun PhoneStep(
    phoneNumber: String,
    onPhoneChange: (String) -> Unit,
    isLoading: Boolean,
    onContinue: () -> Unit
) {
    val isValid = phoneNumber.filter { it.isDigit() }.length == 10

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Giriş Yap",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = TextMain
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Telefon numaranıza doğrulama kodu göndereceğiz.",
            fontSize = 14.sp,
            color = TextMuted,
            textAlign = TextAlign.Center,
            lineHeight = 20.sp
        )

        Spacer(modifier = Modifier.height(36.dp))

        // Telefon input
        OutlinedTextField(
            value = phoneNumber,
            onValueChange = { new ->
                var digits = new.filter { it.isDigit() }
                if (digits.startsWith("0")) {
                    digits = digits.substring(1)
                }
                if (digits.length <= 10) onPhoneChange(digits)
            },
            modifier = Modifier.fillMaxWidth(),
            textStyle = TextStyle(
                fontSize = 14.sp,
                color = TextMain
            ),
            label = { Text("Telefon Numarası") },
            placeholder = { Text("5XX XXX XX XX", fontSize = 13.sp, maxLines = 1, softWrap = false) },
            prefix = { Text("🇹🇷 +90 ", fontSize = 13.sp, color = TextMuted, maxLines = 1, softWrap = false) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { if (isValid) onContinue() }
            ),
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Orange,
                focusedLabelColor = Orange,
                unfocusedBorderColor = BorderClr,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Devam butonu
        Button(
            onClick = onContinue,
            enabled = isValid && !isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Orange,
                disabledContainerColor = Orange.copy(alpha = 0.4f)
            )
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(22.dp),
                    color = Color.White,
                    strokeWidth = 2.5.dp
                )
            } else {
                Text(
                    "Kod Gönder",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Devam ederek Kullanım Koşulları'nı kabul etmiş olursunuz.",
            fontSize = 12.sp,
            color = TextMuted,
            textAlign = TextAlign.Center,
            lineHeight = 16.sp
        )
    }
}

// ─── OTP Adımı ──────────────────────────────────────────────
@Composable
private fun OtpStep(
    phoneNumber: String,
    isLoading: Boolean,
    onVerify: (String) -> Unit,
    onResend: () -> Unit
) {
    val otpLength = 6
    var otpValues by remember { mutableStateOf(List(otpLength) { "" }) }
    val focusRequesters = remember { List(otpLength) { FocusRequester() } }
    val focusManager = LocalFocusManager.current
    var resendTimer by remember { mutableStateOf(60) }
    val scope = rememberCoroutineScope()

    // Geri sayım
    LaunchedEffect(Unit) {
        while (resendTimer > 0) {
            delay(1000)
            resendTimer--
        }
    }

    // Otomatik doğrulama — 6 hane dolunca
    val fullOtp = otpValues.joinToString("")
    LaunchedEffect(fullOtp) {
        if (fullOtp.length == otpLength && otpValues.none { it.isEmpty() }) {
            focusManager.clearFocus()
            onVerify(fullOtp)
        }
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Kodu Girin",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = TextMain
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "+90 $phoneNumber numarasına gönderilen 6 haneli kodu girin.",
            fontSize = 14.sp,
            color = TextMuted,
            textAlign = TextAlign.Center,
            lineHeight = 20.sp
        )

        Spacer(modifier = Modifier.height(36.dp))

        // OTP kutucukları
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            otpValues.forEachIndexed { index, value ->
                OtpCell(
                    value = value,
                    isFocused = false,
                    focusRequester = focusRequesters[index],
                    onValueChange = { newVal ->
                        val digit = newVal.filter { it.isDigit() }.takeLast(1)
                        val updated = otpValues.toMutableList()
                        updated[index] = digit
                        otpValues = updated

                        if (digit.isNotEmpty() && index < otpLength - 1) {
                            focusRequesters[index + 1].requestFocus()
                        }
                    },
                    onBackspace = {
                        val updated = otpValues.toMutableList()
                        if (updated[index].isEmpty() && index > 0) {
                            updated[index - 1] = ""
                            otpValues = updated
                            focusRequesters[index - 1].requestFocus()
                        } else {
                            updated[index] = ""
                            otpValues = updated
                        }
                    },
                    modifier = Modifier.weight(1f).aspectRatio(1f)
                )
            }
        }

        // İlk kutuya otomatik focus
        LaunchedEffect(Unit) {
            focusRequesters[0].requestFocus()
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Doğrula butonu
        Button(
            onClick = { onVerify(fullOtp) },
            enabled = fullOtp.length == otpLength && !isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Orange,
                disabledContainerColor = Orange.copy(alpha = 0.4f)
            )
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(22.dp),
                    color = Color.White,
                    strokeWidth = 2.5.dp
                )
            } else {
                Text(
                    "Doğrula",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Yeniden gönder
        if (resendTimer > 0) {
            Text(
                text = "Kodu yeniden gönder (${resendTimer}s)",
                fontSize = 13.sp,
                color = TextMuted
            )
        } else {
            TextButton(onClick = {
                resendTimer = 60
                scope.launch {
                    while (resendTimer > 0) {
                        delay(1000)
                        resendTimer--
                    }
                }
                onResend()
            }) {
                Text(
                    "Kodu yeniden gönder",
                    fontSize = 13.sp,
                    color = Orange,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

// ─── Tek OTP Hücresi ────────────────────────────────────────
@Composable
private fun OtpCell(
    value: String,
    isFocused: Boolean,
    focusRequester: FocusRequester,
    onValueChange: (String) -> Unit,
    onBackspace: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor = if (value.isNotEmpty()) Orange else BorderClr

    BasicOtpField(
        value = value,
        focusRequester = focusRequester,
        onValueChange = onValueChange,
        onBackspace = onBackspace,
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .border(
                width = if (value.isNotEmpty()) 2.dp else 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(12.dp)
            )
    )
}

@Composable
private fun BasicOtpField(
    value: String,
    focusRequester: FocusRequester,
    onValueChange: (String) -> Unit,
    onBackspace: () -> Unit,
    modifier: Modifier = Modifier
) {
    BasicTextField(
        value = value,
        onValueChange = { new ->
            if (new.isEmpty()) onBackspace()
            else onValueChange(new)
        },
        modifier = modifier.focusRequester(focusRequester),
        textStyle = TextStyle(
            textAlign = TextAlign.Center,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = TextMain
        ),
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.NumberPassword,
            imeAction = ImeAction.Next
        ),
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                innerTextField()
            }
        }
    )
}

// ─── Adım enum'u ────────────────────────────────────────────
private enum class LoginStep { PHONE, OTP }


