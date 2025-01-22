package com.vkolte.doorlocker.ui.screens

import android.app.Activity
import android.content.pm.ActivityInfo
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vkolte.doorlocker.viewmodel.VideoCallViewModel

@Composable
fun VideoCallScreen(
    videoCallViewModel: VideoCallViewModel = viewModel(),
    onCallEnded: () -> Unit = {}
) {
    val context = LocalContext.current
    val activity = context as? Activity
    
    // Make the screen full screen
    DisposableEffect(Unit) {
        activity?.let { act ->
            // Hide system bars and keep screen on
            WindowCompat.setDecorFitsSystemWindows(act.window, false)
            WindowInsetsControllerCompat(act.window, act.window.decorView).apply {
                hide(WindowInsetsCompat.Type.systemBars())
                systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
            act.window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
        
        onDispose {
            activity?.let { act ->
                // Restore system bars and screen timeout
                WindowCompat.setDecorFitsSystemWindows(act.window, true)
                WindowInsetsControllerCompat(act.window, act.window.decorView).apply {
                    show(WindowInsetsCompat.Type.systemBars())
                }
                act.window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            }
        }
    }
    
    // Force landscape orientation
    LaunchedEffect(Unit) {
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    }

    // Handle back press
    BackHandler {
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        activity?.let { act ->
            // Restore all window and system bar settings
            WindowCompat.setDecorFitsSystemWindows(act.window, true)
            WindowInsetsControllerCompat(act.window, act.window.decorView).apply {
                show(WindowInsetsCompat.Type.systemBars())
                show(WindowInsetsCompat.Type.statusBars())
                show(WindowInsetsCompat.Type.navigationBars())
            }
            // Remove fullscreen flags
            act.window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        }
        onCallEnded()
    }

    // Handle toast messages
    LaunchedEffect(videoCallViewModel.toastMessage) {
        videoCallViewModel.toastMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            videoCallViewModel.clearToast()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .systemBarsPadding() // Add padding for system bars when they're shown temporarily
    ) {
        // Video placeholder (replace with actual video implementation)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.DarkGray)
        )

        // Bottom control bar
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(Color.Black.copy(alpha = 0.5f))
                .padding(16.dp)
                .navigationBarsPadding(), // Add padding for navigation bar
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Speaker button
            FloatingActionButton(
                onClick = { videoCallViewModel.toggleSpeaker() },
                shape = CircleShape,
                containerColor = if (videoCallViewModel.isSpeakerOn) 
                    MaterialTheme.colorScheme.primary 
                else MaterialTheme.colorScheme.secondary,
                modifier = Modifier.size(56.dp)
            ) {
                Icon(
                    if (videoCallViewModel.isSpeakerOn) 
                        Icons.Default.VolumeUp 
                    else Icons.Default.VolumeOff,
                    contentDescription = "Speaker"
                )
            }

            // Door Lock button
            FloatingActionButton(
                onClick = { videoCallViewModel.toggleDoorLock() },
                shape = CircleShape,
                containerColor = if (videoCallViewModel.isDoorLocked) 
                    MaterialTheme.colorScheme.error 
                    else MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(56.dp)
            ) {
                Icon(
                    if (videoCallViewModel.isDoorLocked) 
                        Icons.Default.Lock 
                    else Icons.Default.LockOpen,
                    contentDescription = "Door Lock"
                )
            }

            // Hangup button
            FloatingActionButton(
                onClick = { 
                    videoCallViewModel.hangupCall()
                    activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
                    activity?.let { act ->
                        // Restore all window and system bar settings
                        WindowCompat.setDecorFitsSystemWindows(act.window, true)
                        WindowInsetsControllerCompat(act.window, act.window.decorView).apply {
                            show(WindowInsetsCompat.Type.systemBars())
                            show(WindowInsetsCompat.Type.statusBars())
                            show(WindowInsetsCompat.Type.navigationBars())
                        }
                        // Remove fullscreen flags
                        act.window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
                    }
                    onCallEnded()
                },
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(56.dp)
            ) {
                Icon(
                    Icons.Default.CallEnd,
                    contentDescription = "End Call"
                )
            }

            // SOS button
            FloatingActionButton(
                onClick = { videoCallViewModel.triggerSOS() },
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(56.dp)
            ) {
                Icon(
                    Icons.Default.Warning,
                    contentDescription = "SOS"
                )
            }

            // Microphone button
            FloatingActionButton(
                onClick = { videoCallViewModel.toggleMic() },
                shape = CircleShape,
                containerColor = if (videoCallViewModel.isMicOn) 
                    MaterialTheme.colorScheme.primary 
                else MaterialTheme.colorScheme.secondary,
                modifier = Modifier.size(56.dp)
            ) {
                Icon(
                    if (videoCallViewModel.isMicOn) 
                        Icons.Default.Mic 
                    else Icons.Default.MicOff,
                    contentDescription = "Microphone"
                )
            }
        }
    }
} 