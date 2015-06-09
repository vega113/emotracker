class UpdateEmotionCtrl

  constructor: (@$log, @$location, @$routeParams, @EmotionService) ->
    @$log.debug "constructing UpdateEmotionCtrl"
    @emotion = {}
    @findEmotion()


  updateEmotion: () ->
    @$log.debug "updateEmotion()"
    @EmotionService.updateEmotion(@emotion.id, @emotion)
    .then(
      (data) =>
        @$log.debug "updateEmotion: Promise returned #{data} Emotion"
        @Emotion = data
        @$location.path("/")
    ,
      (error) =>
        @$log.error "Unable to update Emotion: #{error}"
    )

  findEmotion: () ->
    id = @$routeParams.id
    # route params must be same name as provided in routing url in app.coffee
    @$log.debug "findEmotion route params: #{id}"

    @EmotionService.listEmotions()
    .then(
      (data) =>
        @$log.debug "Promise returned #{data.length} Emotions"

        # find an emotion with id
        # as filter returns an array, get the first object in it, and return it
        @emotion = (data.filter (emo) ->
          emo.id is parseInt(id)
        )[0]
    ,
      (error) =>
        @$log.error "Unable to get Users: #{error}"
    )


controllersModule.controller('UpdateEmotionCtrl', UpdateEmotionCtrl)