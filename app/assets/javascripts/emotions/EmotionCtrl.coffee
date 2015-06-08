
class EmotionCtrl

    constructor: (@$log, @EmotionService) ->
        @$log.debug "constructing EmotionController"
        @emotions = []
        @getAllEmotions()

    getAllEmotions: () ->
        @$log.debug "getAllEmotions()"

        @EmotionService.listEmotions()
        .then(
            (data) =>
                @$log.debug "Promise returned #{data.length} Emotions"
                @emotions = data
            ,
            (error) =>
                @$log.error "Unable to get Emotions: #{error}"
            )

controllersModule.controller('EmotionCtrl', EmotionCtrl)