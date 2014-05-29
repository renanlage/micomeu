# -*- coding: utf-8 -*-

import json
import logging

from django.http import HttpResponse, HttpResponseBadRequest
from django.views.decorators.csrf import csrf_exempt

from server.models import Story

@csrf_exempt
def sendStory(request):
	# Get json data from request if it's available
	if request.method == 'POST':
		json_data = json.loads(request.body, "utf-8")

		# Save story if it was sent
		if "text" in json_data:
			Story(text = json_data["text"], rating = 0).save()
	
		# Get list of stories read
		ids_read = [int(s) for s in json_data["ids_read"]]

		# Retriving best story not read from database
		try:
			sent_story = Story.objects.exclude(id__in = ids_read)[0]
			# Transform the story model in json format
			sent_story = sent_story.to_json()
		# If user has read all stories, warn him
		except (Story.DoesNotExist, IndexError):
			return HttpResponse("")

		# Send story back in json format through the response object
		response_data = json.dumps(sent_story, ensure_ascii=False)

	return HttpResponse(response_data, content_type="application/json")

@csrf_exempt
def rateStory(request):
	if request.method == 'POST':
		json_data = request.body

		# Store user story in database
		story = json.loads(json_data, "utf-8")

		# Update story rating if user liked it
		if story["like"] == True:
			story = Story.objects.get(id = story["id"])
			story.rating += 1
			story.save()
			
			return HttpResponse("")

	# If something went wrong return a bad request response
	#return HttpResponseBadRequest()
	return HttpResponse("")


