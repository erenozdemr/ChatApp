package com.ex.chatapp.View

import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.ex.chatapp.ViewModel.ChatScreenViewModel
import com.ex.chatapp.ViewModel.VideoCallScreenViewModel
import io.agora.agorauikit_android.AgoraConnectionData
import io.agora.agorauikit_android.AgoraVideoViewer as AgoraVideoViewer


@OptIn(ExperimentalUnsignedTypes::class)
@Composable
fun VideoCallScreen(
    navController: NavController,
    roomName: String,
    agoraid: String,
    viewModel: VideoCallScreenViewModel=remember{VideoCallScreenViewModel()}
){

    var agoraView:AgoraVideoViewer?=null

    val permissonLauncher= rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = {
            viewModel.onPermissonResult(
                acceptedAudiopermisson = it.get(android.Manifest.permission.RECORD_AUDIO)==true,
                acceptedCamerapermisson = it.get(android.Manifest.permission.CAMERA)==true
            )
        }
    )
    LaunchedEffect(key1 = true){
        permissonLauncher.launch(arrayOf(
            android.Manifest.permission.RECORD_AUDIO,
            android.Manifest.permission.CAMERA
        ))
    }

    BackHandler {
        agoraView?.leaveChannel()
        navController.navigateUp()
    }

    if(viewModel.hasAudioPermission.value&&viewModel.hasCameraPermission.value){
        AndroidView(factory = {
            AgoraVideoViewer(it, connectionData = AgoraConnectionData(appId = agoraid)).also {
                it.join(roomName)
                agoraView=it
            }
        },
            modifier = Modifier.fillMaxSize()
        )
    }
    


}

