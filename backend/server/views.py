# -*- coding: utf-8 -*-

import json
import logging

from django.http import HttpResponse, HttpResponseBadRequest
from django.views.decorators.csrf import csrf_exempt

from server.models import Story, Rating, Vote

@csrf_exempt
def sendStory(request):
	# Get json data from request if it's available
	if request.method == 'POST':
		json_data = json.loads(request.body, "utf-8")

		# Save story if it was sent
		if "text" in json_data:
			story = Story(text = json_data["text"])
			story.save()			
	
		# Get list of stories read
		ids_read = [int(s) for s in json_data["ids_read"]]

		# Retriving best story not read from database
		try:
			# Get all stories not read
			stories = Story.objects.exclude(id__in = ids_read)
			# Raise exception if no story was found
			if not stories:
				raise Story.DoesNotExist

			# Get story with the highest score and the number of ratings
			nratings = Rating.objects.all().count()
			best_story, score = getBestStory(stories)

			# Transform the story model in json format
			story_data = best_story.to_json()
			story_data["score"] = score
			story_data["nratings"] = nratings
		# If user has read all stories, warn him
		except (Story.DoesNotExist, IndexError):
			return HttpResponse("")

		# Send story back in json format through the response object
		response_data = json.dumps(story_data, ensure_ascii=False)

	return HttpResponse(response_data, content_type="application/json")

def getBestStory(stories):
	max_score = 0
	best_story = None
	for story in stories:
		score = getScore(story)
		if score > max_score:
			max_score = score
			best_story = story
	return best_story, max_score

def getScore(story):
	# If there is no vote return 0 as score
	votes = Vote.objects.filter(story = story)
	if not votes:
		return 0
	# Calculate the score for the story, range 0-10
	nratings = Rating.objects.all().count()
	nvotes = 0
	sum = 0
	for vote in votes:
		nvotes += 1
		sum += vote.rating.value
	return int(round((sum * (10.0 / nratings)) / nvotes))

@csrf_exempt
def rateStory(request):
	if request.method == 'POST':
		json_data = request.body

		# Store user story in database
		story_data = json.loads(json_data, "utf-8")

		# Save new story rating
		if story_data['rating'] >= 0:
			print story_data
			story = Story.objects.get(id = story_data["id"])
			rating = Rating.objects.get(value = story_data["rating"])
			Vote(story = story, rating = rating).save()
			
			return HttpResponse("")

	# If something went wrong return a bad request response
	#return HttpResponseBadRequest()
	return HttpResponse("")


