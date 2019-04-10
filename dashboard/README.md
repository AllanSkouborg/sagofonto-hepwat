


# "Preflight" check – Angular CLI installation
You will need the Angular CLI (Command Line Interface) tool to build  and run the Angular app. Check if Angular is installed on your system by typing in your terminal/console

`ng --version`

If no version information appears, follow the steps below to install Angular CLI. Otherwise, skip to "Install and build Dashboard app".

Make sure you have the npm package manager installed. In your terminal/console, type: 

`npm -v`

You should get a version number as a result.

If you do not, you must install Node.js. Go to https://nodejs.org/en/download/, download the proper installer for your OS and follow the instructions. Once again, check that npm has been installed as part of Node.js

`npm -v`

If a version number shows up, npm is ready.

Install Angular CLI

`npm install -g @angular/cli`

To get more help on the Angular CLI use `ng help` or go check out the [Angular CLI README](https://github.com/angular/angular-cli/blob/master/README.md).

# Install and build Dashboard app

You will need to install the necessary node modules by navigating to the app main folder and typing

`npm install`

This might take a while as a lot of libraries are being fetched.

When it’s done, you should see a new folder called node_modules, and it should be filled with a host of different libraries and modules.

Now check that the app is operational by compiling and running a live development version on localhost, port 4200, by typing:

`ng serve`

When the localhost app has compiled and is ready, open your browser and go to the URL  http://localhost:4200
Port 4200 is the default. You can define another port by adding the port-option, for example:

`ng serve –-port=2398`

To build the production version of the app, type:

`ng build –-prod –-base-href=”/website/path/to/app/folder/”`

where the base-href option could be something like “/dashboard/” if for example the dashboard URL is https://example.com/dashboard?id=38. 

Please note, that the path has to have a forward slash (“/”) at the end of it.

If the build is successful, the production version will be available in the "dist/production" folder in the Angular project folder.
