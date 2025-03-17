# Night Widget

Simple examples of a couple of ways to try to react directly to night mode
changes in an App Widget.

It should be noted first that some `RemoteViews` methods have overloads with
separate options for `notNight`/`night` – e.g., [`setIcon()`][setIcon] – which
can handle resource swaps automatically, but not until API level 31.

Also, some launchers and other hosts may already handle updating existing
Widgets upon a mode change, but many, if not most, seemingly do not.

The following sections describe two different Widget classes, but they share a
common `RemoteViews` setup that contains a single `TextView` as a simple label.
The `TextView`'s background and text colors are swapped between white and black
upon a change in order to indicate the current mode.

Both approaches can be used in a single Widget. They're separate here only to
clearly delineate the relevant setup for each.

## Content URI trigger

- [`ContentUriTriggerWidget`][ContentUriTriggerWidget]
- [`ContentUriTriggerWorker`][ContentUriTriggerWorker]

This example uses `JobScheduler`/`WorkManager`'s content URI trigger
functionality, which wasn't introduced until API level 24, so that's the
`minSdk` for this approach.

The specific `Settings` value that's monitored – `ui_night_mode` – is
undocumented and excluded from the SDK, so it probably won't work everywhere,
but it should fail silently wherever it doesn't.

Some devices/manufacturers will have different and/or additional values that
change during a mode switch – e.g, https://stackoverflow.com/a/51909594 – so you
might be able to find other options for certain setups if `ui_night_mode`
doesn't work.

If you'd like to look for yourself, you can set a `ContentObserver` on
`Settings.System.CONTENT_URI` or `Settings.Secure.CONTENT_URI` to see what
changes on a particular device. You don't need to read the value from
`Settings`, only when it changes, so this method shouldn't require any special
permissions. You should still account for possible `Exception`s if using this in
production, however.

Do note that this combination of a `Worker` with an App Widget can be finicky
and fragile during development. Certain debug operations like "Apply Changes…",
or merely stopping the app from the IDE, can cause things to break completely,
and you might have to clear the data or uninstall/reinstall more than usual.
Just a heads up.

## Options change

- [`OptionsChangeWidget`][OptionsChangeWidget]

Some hosts will call `onAppWidgetOptionsChanged()` upon a night mode switch,
though, again, that's not standard behavior. Tracking the mode lets us determine
if the current call is for a mode change, in which case we then call through to
`onUpdate()`.

This approach does not appear to work at all on any of the official emulators.
However, when it does work, it is much faster than the other one, since there's
no waiting for a Worker to spin up.

<br />

## License

MIT License

Copyright (c) 2025 Mike M.

Permission is hereby granted, free of charge, to any person obtaining a copy of
this software and associated documentation files (the "Software"), to deal in
the Software without restriction, including without limitation the rights to
use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
the Software, and to permit persons to whom the Software is furnished to do so,
subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.


[ContentUriTriggerWidget]: app/src/main/kotlin/com/gonodono/nightwidget/ContentUriTriggerWidget.kt

[ContentUriTriggerWorker]: app/src/main/kotlin/com/gonodono/nightwidget/ContentUriTriggerWorker.kt

[OptionsChangeWidget]: app/src/main/kotlin/com/gonodono/nightwidget/OptionsChangeWidget.kt

[setIcon]: https://developer.android.com/reference/android/widget/RemoteViews#setIcon(int,%20java.lang.String,%20android.graphics.drawable.Icon,%20android.graphics.drawable.Icon)