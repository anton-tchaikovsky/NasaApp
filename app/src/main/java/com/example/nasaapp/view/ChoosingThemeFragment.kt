package com.example.nasaapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.nasaapp.R
import com.example.nasaapp.databinding.CardViewForChoosingThemeBinding
import com.example.nasaapp.databinding.ChoosingThemeLayoutBinding
import com.example.nasaapp.utils.KEY_CHOOSING_THEME
import com.example.nasaapp.utils.KEY_CHOSEN_THEME
import com.example.nasaapp.utils.Theme
import com.example.nasaapp.utils.prepareMenu

class ChoosingThemeFragment : Fragment() {

    companion object {
        fun newInstance() = ChoosingThemeFragment()
    }

    private var _binding: ChoosingThemeLayoutBinding? = null
    private val binding get() = _binding!!

    private val onClickListenerButtonApply: View.OnClickListener = View.OnClickListener {
        requireActivity().supportFragmentManager.setFragmentResult(
            KEY_CHOOSING_THEME,
            Bundle().apply {
                putString(KEY_CHOSEN_THEME, it.tag.toString())
            })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ChoosingThemeLayoutBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        settingCardChoosingTheme(binding.cardRed, Theme.THEME_RED)
        settingCardChoosingTheme(binding.cardBlue, Theme.THEME_BLUE)
        settingCardChoosingTheme(binding.cardOrange, Theme.THEME_ORANGE)
        settingButtonBack()
        prepareMenu(requireActivity() as ActivityNasaApp, viewLifecycleOwner)
    }

    // метод настраивает отображение cardView и вешает слушателя на buttonApply в составе cardView
    private fun settingCardChoosingTheme(cardView: CardViewForChoosingThemeBinding, theme:Theme){
        cardView.apply {
            buttonApply.apply {
                tag=theme.title
                setOnClickListener(onClickListenerButtonApply)
                contentDescription = "${R.string.apply} ${theme.title}"
                // не работает
                // backgroundTintList = ColorStateList(arrayOf(intArrayOf()),intArrayOf(theme.idColorPrimaryContainer))
                backgroundTintList = when(theme){
                    Theme.THEME_RED -> {
                        setTextColor(ContextCompat.getColorStateList(requireContext(), R.color.button_apply_text_color_theme_red))
                    ContextCompat.getColorStateList(requireContext(), R.color.button_apply_background_color_theme_red)
                    }
                    Theme.THEME_BLUE ->{
                        setTextColor(ContextCompat.getColorStateList(requireContext(), R.color.button_apply_text_color_theme_blue))
                    ContextCompat.getColorStateList(requireContext(), R.color.button_apply_background_color_theme_blue)
                    }
                    Theme.THEME_ORANGE -> {
                        setTextColor(ContextCompat.getColorStateList(requireContext(), R.color.button_apply_text_color_theme_orange))
                    ContextCompat.getColorStateList(requireContext(), R.color.button_apply_background_color_theme_orange)
                    }
                }
            }
            themeTitle.text = theme.title
            viewPrimaryColor.setBackgroundColor(ContextCompat.getColor(requireContext(),theme.idColorPrimary))
            viewPrimaryContainerColor.setBackgroundColor(ContextCompat.getColor(requireContext(),theme.idColorPrimaryContainer))
            viewSurfaceColor.setBackgroundColor(ContextCompat.getColor(requireContext(),theme.idColorSurface))
        }
    }

    // метод устанавливает слушателя на ButtonBack
    private fun settingButtonBack(){
        binding.buttonBack.setOnClickListener{
            requireActivity().onBackPressed()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}