# -*- coding: utf-8 -*-

import json

from django.http import HttpResponse

from server.models import Story

def process(request):

	# Get json data from request if it's available
	if request.method == 'POST':
		json_data = request.body
		# Store user story in database
		data = json.loads(json_data)
		Story(text = data["text"], rating = 0).save()

	# Retriving best story not read from database
	id_read = [1,2]
	try:
		sent_story = Story.objects.exclude(id__in = id_read)[0]
		# Transform the story model in json format
		sent_story = sent_story.to_json()
	# If user has read all stories, warn him
	except (Story.DoesNotExist, IndexError):
		sent_story = {'text': u"Você já viu todas as histórias! Chame seus amigos para compartilhar a deles!"}

	# Send story back in json format through the response object
	response_data = json.dumps(sent_story, ensure_ascii=False)
	return HttpResponse(response_data, content_type="application/json")