# -*- coding: utf-8 -*-

import json
import logging

from django.http import HttpResponse, HttpResponseBadRequest
from django.views.decorators.csrf import csrf_exempt

from server.models import Story, Rating

@csrf_exempt
def sendStory(request):
	# Get json data from request if it's available
	if request.method == 'POST':
		json_data = json.loads(request.body, "utf-8")

		# Save story if it was sent
		if "text" in json_data:
			story = Story(text = json_data["text"])
			story.save()
			Rating(story = story, score = 0, nratings = 0).save()
	
		# Get list of stories read
		ids_read = [int(s) for s in json_data["ids_read"]]

		# Retriving best story not read from database
		try:
			sent_story = Story.objects.exclude(id__in = ids_read).order_by("-rating__score")[0]
			story_rating = sent_story.rating

			# Transform the story model in json format
			story_data = sent_story.to_json()
			story_data["rating"] = story_rating.score
		# If user has read all stories, warn him
		except (Story.DoesNotExist, IndexError):
			return HttpResponse("")

		# Send story back in json format through the response object
		response_data = json.dumps(story_data, ensure_ascii=False)

	return HttpResponse(response_data, content_type="application/json")

@csrf_exempt
def rateStory(request):
	if request.method == 'POST':
		json_data = request.body

		# Store user story in database
		story_data = json.loads(json_data, "utf-8")

		# Save new story rating
		if story_data['rating']:
			story = Story.objects.get(id = story_data["id"])
			rating = story.rating
			rating.add(story_data["rating"])
			rating.save()
			
			return HttpResponse("")

	# If something went wrong return a bad request response
	#return HttpResponseBadRequest()
	return HttpResponse("")


