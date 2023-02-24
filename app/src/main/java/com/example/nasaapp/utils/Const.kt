package com.example.nasaapp.utils

import com.example.nasaapp.BuildConfig

//RemoteDataSource
const val END_POINT_ASTRONOMY_PICTURE_OF_THE_DAY = "planetary/apod"
const val END_POINT_MARS_ROVER_PHOTOS = "mars-photos/api/v1/rovers/curiosity/photos"
const val END_POINT_EARTH_POLYCHROMATIC_IMAGING_CAMERA = "EPIC/api/natural"
const val API_NASA_KEY = "api_key"
const val DATE = "date"
const val EARTH_DATE = "earth_date"
const val URL_API_NASA ="https://api.nasa.gov"
const val URL_EARTH_PHOTO = "https://api.nasa.gov/EPIC/archive/natural/%s/png/%s.png?api_key=%s"
const val TIME_ZONE_NEW_YORK = "America/New_York"
const val DISCONNECT_NETWORK = "Отсутствует подключение к сети"
const val LOADING_ERROR = "Ошибка загрузки данных"
const val TAG_APP = "@@@"
val DEBUG = BuildConfig.DEBUG
//TAG fragments
const val TAG_ASTRONOMY_PICTURES_OF_THE_DAY_FRAGMENT = "TAGAstronomyPicturesOfTheDayFragment"
const val TAG_HD_ASTRONOMY_PICTURES_OF_THE_DAY_FRAGMENT = "TAGHdAstronomyPicturesOfTheDayFragment"
const val TAG_CHOOSING_THE_DAY_FRAGMENT = "TAGChoosingTheDayFragment"
const val TAG_CHOOSING_THEME_FRAGMENT = "TAGChoosingThemeFragment"
const val TAG_BOTTOM_MENU_NAVIGATION_DRAWER_FRAGMENT = "TAGBottomMenuNavigationDrawerFragment"
const val TAG_VIEW_PAGER_FOR_MARS_ROVER_PHOTOS_FRAGMENT = "TAGViewPagerForMarsRoverPhotosFragment"
const val TAG_MARS_ROVER_PHOTOS_RECYCLER_VIEW_FRAGMENT = "TAGMarsRoverPhotosRecyclerViewFragment"
const val TAG_EARTH_PHOTOS_FRAGMENT = "TAGEatherPhotosFragment"
const val KEY_TAG_CURRENT_FRAGMENT = "KeyTagCurrentFragment"
//Name fragment
const val ASTRONOMY_PICTURE_OF_THE_DAY= "Astronomy pictures of the day"
const val EARTH_PHOTO = "Eather photo"
const val MARS_ROVER_PHOTO = "Mars rover photo"
const val CHOOSING_THEME = "Choosing theme"
//Theme
const val THEME_RED_TITLE = "Theme Red"
const val THEME_BLUE_TITLE = "Theme Blue"
const val THEME_ORANGE_TITLE = "Theme Orange"
const val KEY_CHOOSING_THEME = "KeyChoosingTheme"
const val KEY_CHOSEN_THEME = "KeyChosenTheme"
const val KEY_SAVED_THEME = "KeySavedTheme"
const val LIGHT_THEME_ENABLED = "Включена светлая тема"
const val DARK_THEME_ENABLED = "Включена темная тема"
//All
const val REQUIRE_KEY_ASTRONOMY_PICTURES_OF_THE_DAY = "RequireKeyAstronomyPicturesOfTheDayFragment"
const val KEY_ASTRONOMY_PICTURES_OF_THE_DAY = "KeyAstronomyPicturesOfTheDayFragment"
const val KEY_DAY = "KeyDay"
const val KEY_MARS_ROVER_PHOTO = "KeyMarsRoverPhoto"
const val TODAY = "Today"
const val YESTERDAY = "Yesterday"
const val DAY_BEFORE_YESTERDAY = "Two days ago"
const val URI_YANDEX_SEARCH = "https://yandex.ru/search/?text="
const val KEY_EXPANDED_TOOLBAR = "KeyExpandedToolbar"
const val DURATION = 1000L
const val MAST = "Mast Camera"
const val FHAC = "Front Hazard Avoidance Camera"
const val RHAZ = "Rear Hazard Avoidance Camera"

