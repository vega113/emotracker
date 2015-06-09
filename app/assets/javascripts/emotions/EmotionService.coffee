
class EmotionService

    @headers = {'Accept': 'application/json', 'Content-Type': 'application/json'}
    @defaultConfig = { headers: @headers }

    constructor: (@$log, @$http, @$q) ->
        @$log.debug "constructing EmotionService"

    listEmotions: () ->
        @$log.debug "listEmotions()"
        deferred = @$q.defer()

        @$http.get("/emotion")
        .success((data, status, headers) =>
                @$log.info("Successfully listed Emotions - status #{status}")
                deferred.resolve(data)
            )
        .error((data, status, headers) =>
                @$log.error("Failed to list Emotions - status #{status}")
                deferred.reject(data)
            )
        deferred.promise

    createEmotion: (Emotion) ->
        @$log.debug "createEmotion #{angular.toJson(Emotion, true)}"
        deferred = @$q.defer()

        @$http.post('/emotion', Emotion)
        .success((data, status, headers) =>
                @$log.info("Successfully created Emotion - status #{status}")
                deferred.resolve(data)
            )
        .error((data, status, headers) =>
                @$log.error("Failed to create Emotion - status #{status}")
                deferred.reject(data)
            )
        deferred.promise

    updateEmotion: (id, Emotion) ->
      @$log.debug "updateEmotion #{angular.toJson(Emotion, true)}"
      deferred = @$q.defer()

      @$http.put("/emotion/#{id}", Emotion)
      .success((data, status, headers) =>
              @$log.info("Successfully updated Emotion - status #{status}")
              deferred.resolve(data)
            )
      .error((data, status, header) =>
              @$log.error("Failed to update Emotion - status #{status}")
              deferred.reject(data)
            )
      deferred.promise

servicesModule.service('EmotionService', EmotionService)