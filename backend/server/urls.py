from django.conf.urls import patterns, include, url

urlpatterns = patterns('',
    url(r'^send$', 'server.views.sendStory', name='sendStory'),
    url(r'^rate$', 'server.views.rateStory', name='rateStory'),
)