Pod::Spec.new do |s|
  s.name         = "HyperswitchResources"
  s.version      = "0.0.1"
  s.summary      = "Resources for Hyperswitch"
  s.description  = "Resources bundle for Hyperswitch React Native app"
  s.homepage     = "https://hyperswitch.io"
  s.license      = "MIT"
  s.author       = { "Hyperswitch" => "support@hyperswitch.io" }
  s.platform     = :ios, "13.0"
  s.source       = { :git => ".", :tag => s.version.to_s }
  s.resources    = "**/*"
end
