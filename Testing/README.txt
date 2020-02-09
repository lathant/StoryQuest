
with regard to "uOttaHack-autism-gcloud_credentials-0bd2ffc7263a.json"
- this file is used by @google-cloud/XXX npm apis
- in order for it to work, you need to set the evironment variable first
- e.g.) On Linux/macOS (bash)
	export GOOGLE_APPLICATION_CREDENTIALS="/home/user/Downloads/[FILE_NAME].json"

	On Windows (powershell)
	$env:GOOGLE_APPLICATION_CREDENTIALS="C:\Users\username\Downloads\[FILE_NAME].json"

	On Windows (cmd)
	set GOOGLE_APPLICATION_CREDENTIALS=[PATH]

- The environment variable will dissapear at the end of the console/terminal session
