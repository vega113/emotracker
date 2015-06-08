class UpdateEmotionCtrl

  constructor: (@$log, @$location, @$routeParams, @EmotionService) ->
    @$log.debug "constructing UpdateEmotionCtrl"
    @emotion = {}
    @findEmotion()

  updateEmotion: () ->
  @$log.debug "updateEmotion()"
  @EmotionService.updateEmotion(@emotion)
  .then(
    (data) =>
      @$log.debug "Promise returned #{data} Emotion"
      @Emotion = data
      @$location.path("/")
  ,
    (error) =>
      @$log.error "Unable to update Emotion: #{error}"
  )

  findEmotion: () ->
  # route params must be same name as provided in routing url in app.coffee
  id = @$routeParams.id
  @$log.debug "findEmotion route params: #{id}"

  @EmotionService.listEmotions()
  .then(
    (data) =>
      @$log.debug "Promise returned #{data.length} Users"

      # find an emotion with id
      # as filter returns an array, get the first object in it, and return it
      @emotion = (data.filter (emotion) -> emotion.id is id)[0]
  ,
    (error) =>
      @$log.error "Unable to get Users: #{error}"
  )



controllersModule.controller('UpdateEmotionCtrl', UpdateEmotionCtrl)