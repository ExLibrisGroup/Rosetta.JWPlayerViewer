rosetta.JWPlayer
================

Instructions for using JW Player as an external viewer:

1) Download jwplayer and add the following to the video.player.viewer project:

• Under the flash directory, add the "player.swf" file.

• Under the JWPlayer folder, add the "jwplayer.js" file (in some versions this file is named jwplayer.min.js - change the file name to jwplayer.js).

Both files should be extracted from this ZIP

Note: Viewer does not work with the new JWPlayer (version 6) since it is HTML5 based, and Rosetta's implementation uses the flash option. We recommend using the 5.10 version, used for development and testing.

2) Deploy war on an application server.

We recommend using an external application server. Ex Libris does not guarantee future changes to the Rosetta JBoss AS will be compatible with the player. If you do deploy the viewer on an external server, you will need to configure the viewer.properties file situated under the conf folder: change localhost to your Rosetta server name.

3) Configure JW Player as a Rosetta Viewer

The viewer can be configured as either an IE or a FILE viewer.

URLs for configuring as an external viewer:

•Video viewer: http://{host}:1801/{warName}/videoPlayerViewer?provider=VIDEO (e.g.: http://externalapplicationserver:1801/video-player-viewer/videoPlayerViewer?provider=VIDEO)

•Audio viewer: http://{host}:1801/{warName}/videoPlayerViewer?provider=AUDIO (e.g.: http://externalapplicationserver:1801/video-player-viewer/videoPlayerViewer?provider=AUDIO)
