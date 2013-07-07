# mob-site-clojure

Generates a web site customized for mobile devices.

Project is part of a series on Polyglot programming. Each article in the series describes
the process of implementing and enhancing the project application, in a different computer
language. This is the Cloure implemenation.

See my [website, Public-Action.org] (http://public-action.org/mob/polyglot-index.html)  to read the articles series.
## Usage
Requirements/Dependencies

The easiest way to use this application is to intall [Clojure] (http://clojure.org). 
and [Leiningen] (http://http://leiningen.org/). This application requires the [Apache Commons IO] 
(http://commons.apache.org/proper/commons-io/) library.

Documentation generated with the Leiningen plugin [Codox] (https://github.com/weavejester/codox)

Usage

From a terminal (E.G. xterm), change into the application's root directory.
Issue the following commaand line:

$mob-site-gen> lein -in RSS_INPUT_FILE -out OUTPUTDIR

## License
Code licensed under [GNU General Public License, version 2] (http://www.gnu.org/licenses/gpl-2.0.html")

## License
Written by Lorin M Klugman

## Development Notes

To run tests, issue the following command line:
$> lein test

To generate documentation, issue the following command line:
$> lein doc

