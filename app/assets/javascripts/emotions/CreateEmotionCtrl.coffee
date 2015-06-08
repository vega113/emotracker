
class CreateEmotionCtrl

    constructor: (@$log, @$location,  @EmotionService) ->
        @$log.debug "constructing CreateEmotionController"
        @Emotion = {}

    createEmotion: () ->
        @$log.debug "createEmotion()"
        @EmotionService.createEmotion(@Emotion)
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