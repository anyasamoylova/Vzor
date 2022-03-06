package software.girls.vzor.screen.home

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import software.girls.vzor.R
import software.girls.vzor.checkCameraPermission
import software.girls.vzor.checkOverlayPermission
import software.girls.vzor.databinding.FragmentHomeBinding
import software.girls.vzor.screen.floating_camera.FloatingWindowService
import software.girls.vzor.screen.home.elm.Effect
import software.girls.vzor.screen.home.elm.Event
import software.girls.vzor.screen.home.elm.State
import software.girls.vzor.screen.home.elm.StoreFactory
import vivid.money.elmslie.android.base.ElmFragment
import vivid.money.elmslie.core.store.Store

class HomeFragment private constructor(): ElmFragment<Event, Effect, State>(R.layout.fragment_home) {

    //TODO create view binding delegate
    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding get() = _binding!!

    override val initEvent = Event.Ui.Init

    override fun createStore(): Store<Event, Effect, State> = StoreFactory.create()

    override fun render(state: State) {
        //TODO
    }

    override fun handleEffect(effect: Effect) =
        when (effect) {
            Effect.OpenFloatingWindow -> {
                startFloatingWindowService()
            }
            is Effect.ShowError -> {
                showError(effect.error)
            }
        }

    private val activityResultLauncherCameraPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            //startCamera()
        } else {
            showError("Permission is not granted by user")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnStartService.setOnClickListener {
            store.accept(Event.Ui.OpenFloatingWindowClick)
        }
    }

    private fun askForOverlayPermission() {
        val intent = Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:${requireActivity().packageName}")
        )
        startActivityForResult(intent, AppCompatActivity.RESULT_OK)
    }

    private fun showError(error: String) {
        Toast.makeText(requireActivity(), error, Toast.LENGTH_SHORT)
            .show()
    }

    private fun startFloatingWindowService() {
        if (requireContext().checkCameraPermission()) {
            if (requireContext().checkOverlayPermission()) {
                requireActivity().startService(
                    Intent(
                        requireActivity(),
                        FloatingWindowService::class.java
                    )
                )
                requireActivity().finish()
            } else {
                askForOverlayPermission()
            }
        } else {
            activityResultLauncherCameraPermission.launch(Manifest.permission.CAMERA)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        fun newInstance() = HomeFragment()
    }
}