from django.conf.urls import patterns, include, url

urlpatterns = patterns('',
    url(r'^$', 'server.views.process', name='process'),
)