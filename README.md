#  📲 Shgardi Android Developer Task

An Android app showcasing popular people in the entertainment industry using the TMDB API. Features include infinite scrolling, search functionality, and detailed person profiles.

## 🚀 Features
- **Search**: Dynamically search for people with real-time results.
- **Details View**: Display personal info and a grid of images.
- **Image Loading**: Efficiently load images with Glide and custom BindingAdapter.
- **Popular People List**: Infinite scrolling using a custom scroll listener.

## ⚙ Technologies & Libraries
- **MVVM Architecture**: Separation of concerns with ViewModel and LiveData.
- **Retrofit 2**: Network requests to TMDB API.
- **Glide**: Image loading and caching.
- **DataBinding**: UI binding with custom adapters.

## 📷 Screenshots
<img src ="./readme/Home%20Screen.png" width="200" />&nbsp;&nbsp;<img src ="./readme/SearchResult%20Screen%202.png" width="200" />
<img src ="./readme/SearchResult%20Screen.png" width="200" />
<img src ="./readme/PersonInfo%20Screen.png" width="200" />


## 📷 RTL Supported Screenshots
<img src ="./readme/RTLHomeScreen.png" width="200" />&nbsp;&nbsp;<img src ="./readme/RTLSearchScreen.png" width="200" />
<img src ="./readme/RTLInfoScreen.png" width="200" />

## 💻 Permissions
- Internet

## 📝 Image Loading with Glide and custom BindingAdapter

```
@JvmStatic
@BindingAdapter("load_image_with_prefix")
fun loadImageWithPrefix(imageView: ImageView, url: String?) {
    Glide.with(imageView.context)
        .load(WebApiInterface.PERSON_IMG_PREFIX+url) // image url
        .placeholder(R.drawable.placeholder_dark) // any placeholder to load at start
        .error(R.drawable.placeholder_dark) // any image in case of error
        .override(512, 512) // resizing
        .centerCrop()
        .into(imageView)
}
```


![forthebadge](https://forthebadge.com/images/badges/built-with-love.svg)
![ForTheBadge ANDROID](https://forthebadge.com/images/badges/built-for-android.svg)
![ForTheBadge GIT](https://forthebadge.com/images/badges/uses-git.svg)
