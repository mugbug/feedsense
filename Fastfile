# More documentation about how to customize your build
# can be found here:
# https://docs.fastlane.tools
fastlane_version "2.68.0"

# This value helps us track success metrics for Fastfiles
# we automatically generate. Feel free to remove this line
# once you get things running smoothly!
generated_fastfile_id "f1162a86-a27e-4260-b1e6-267169127965"

default_platform :android

# Fastfile actions accept additional configuration, but
# don't worry, fastlane will prompt you for required
# info which you can add here later
lane :beta do
  # build the release variant
  build_android_app(task: "assembleRelease")

  # upload to Beta by Crashlytics
  crashlytics(
    api_token: "f4f20d1c10f8b0474509e1198eb1097a3493cfe8",
    build_secret: "7c7eec2e9181427f4ee5866dfa7348ee07f874e81dee2882f32e4a23a780d5fd"
  )
end
