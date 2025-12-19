Pod::Spec.new do |s|
  s.name         = "hyperswitch-sdk-ios"
  s.version      = "0.0.1"
  s.summary      = "Resources for Hyperswitch"
  s.description  = "Resources bundle for Hyperswitch React Native app"
  s.homepage     = "https://hyperswitch.io"
  s.license      = "MIT"
  s.author       = { "Hyperswitch" => "support@hyperswitch.io" }
  s.platform     = :ios, "13.0"
  s.source       = { :git => ".", :tag => s.version.to_s }

  s.subspec 'core' do |core|
    core.source_files = 'hyperswitchSDK/Core/**/*.{m,swift,h}'
    core.resources = ['hyperswitchSDK/Core/Resources/HyperOTA.plist', "HyperSwitch/resources/**"]
    core.vendored_frameworks = 'frameworkgen/Frameworks/Core/*.xcframework'
    core.dependency 'hyperswitch-sdk-ios/common'
    # core.dependency 'hyperswitch-ios-hermes', '0.79.1'
    core.dependency 'HyperOTA', '0.0.8'
  end

  # s.subspec 'sentry' do |sentry|
  #   sentry.vendored_frameworks = 'frameworkgen/Frameworks/Sentry/*.xcframework'
  #   sentry.dependency 'hyperswitch-sdk-ios/core'
  # end

  # s.subspec 'scancard' do |scancard|
  #   scancard.source_files = 'frameworkgen/scanCard/Source/**/*.{m,swift,h}'
  #   scancard.vendored_frameworks = 'frameworkgen/scanCard/Frameworks/*.xcframework'
  #   scancard.dependency 'hyperswitch-sdk-ios/core'
  # end

  # s.subspec 'netcetera3ds' do |netcetera3ds|
  #   netcetera3ds.source_files = 'frameworkgen/3ds/Source/**/*.{m,swift,h}'
  #   netcetera3ds.vendored_frameworks = 'frameworkgen/3ds/Frameworks/*.xcframework'
  #   netcetera3ds.dependency 'hyperswitch-sdk-ios/core'
  # end

  s.subspec 'common' do |common|
    common.source_files = 'hyperswitchSDK/Shared/**/*.{m,swift,h}'
  end

  s.default_subspec = 'core', 'common'
end
