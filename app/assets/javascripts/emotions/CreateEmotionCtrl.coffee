
class CreateEmotionCtrl

    constructor: (@$log, @$location,  @EmotionService) ->
        @$log.debug "constructing CreateEmotionController"
        @emotion = {}

    createEmotion: () ->
        @$log.debug "createEmotion()"
        @EmotionService.createEmotion(@emotion)
        .then(
            (data) =>
                @$log.debug "Promise returned #{data} Emotion"
                @Emotion = data
                @$location.path("/")
            ,
            (error) =>
                @$log.error "Unable to create Emotion: #{error}"
            )

controllersModule.controller('CreateEmotionCtrl', CreateEmotionCtrl)