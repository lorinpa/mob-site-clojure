(ns mob-site.docFormatter
^{:doc "Simple html page template/generator."
       :author "Lorin M Klugman"}
(:use mob-site.parseFeed)
)



(defn create-meta-keywords"generates an html meta-tag of keywords. \n parm category-set is a collection of terms derived from the RSS XML doc" [category-set]
	
	(let  [category-count (count category-set)]
		(cond
			(= 0 category-count)
			(str "<meta name=\"keywords\" content=\"Web Development, Full-Stack, Mobile, JavaScript, JVM, Testing, Documentation, Chicago, Full Stack\" /> " )
				
			(= 1  category-count)
			(str "<meta name=\"keywords\" content=\"Web Development, Full-Stack, Mobile, JavaScript, JVM, Testing, Documentation, Chicago, Full Stack," (first category-set) "\"/> ")
				
			(>  category-count 1 )
			(str "<meta name=\"keywords\" content=\"Web Development, Full-Stack, Mobile, JavaScript, JVM, Testing, Documentation, Chicago, Full Stack, " 	
						(clojure.string/join "," category-set ) " \"/>")
			
			:default ""
		)
	)
)


(defn webDocFormat "generates a complete html document. 
	parms:  
	-docNode represents the document fragments derived from the RSS XML doc. 
	-linkText and -href define a navigation link. 
	-homeLinks sets whether we add links to the sites index.html"
 
		[docNode linkText href homeLinks]

	( let	[	meta-tags  (create-meta-keywords (:categories docNode))
		 
		style
		"<style>
				.wow {
					color:white;
				}
				.wee {
					color:gold;
				}
				.contact:hoover:after {
					content: \"<div>lorinmk@public-action.org</div>\";
				}
				#contactbox {
					font-weight: bold;
					margin-left: -10px;
					width:90%;
					background:white;
					box-shadow:10px 10px 5px #888;
				}
				@media all and (max-width: 480px) {
					.greeting {
							margin-bottom: 5px;
					}
				}
				</style>"
	
				contactBoxJS 
                "<script>
						var ContactBox = function(target_el) {
								this.init = function(target_el) {
									this.targel_el = target_el;
									this.state = false;
								};
								this.render = function() {
									var msg = \"lorinmk @ public-action.org\";
									var div = document.createElement(\"div\");
									div.setAttribute(\"class\", \"alert alert-block alert-info\");
									div.setAttribute(\"id\", \"contactbox\");
									div.textContent = msg;
									var cache = this.targel_el.innerHTML;
									this.targel_el.innerHTML = div.outerHTML + cache;
								};
								this.hide = function() {
									var contactBox = document.querySelector(\"#contactbox\");
									this.targel_el.removeChild(contactBox);
								};
								this.toggle = function() {
										if (this.state === true) {
											this.hide();
											this.state = false;
										} else {
											this.render();
											this.state = true;
										}
								};
								this.init(target_el);
						};
						var ContactListener = function(event) {
							event.preventDefault();
							var origin = event.target.tagName;
							contactBox.toggle();
						};
				</script>"
	
				docReadyJS 
               " <script>
                    document.onreadystatechange = function () {
                        if (document.readyState === \"complete\") {
                            var contactDiv = document.querySelector(\"#contact\");
                            contactBox = new ContactBox(contactDiv);
                            contactDiv.addEventListener(\"click\", ContactListener, true); 
                        }
                    };
                </script>"
                
                 templateStr
                 (str
					"<!DOCTYPE html>
					 <html>
						<head>
						<title>%s</title>
						<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"/>
						<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>
						<link type=\"text/css\" href=\"css/bootstrap/css/bootstrap.min.css\" media=\"all\" rel=\"stylesheet\" id=\"bootstrap\"/>
							%s
							%s
						<link rel=\"shortcut icon\" href=\"favicon.png\"/>
						<link rel=\"prerender\" href=\"http://public-action.org/mob/index.html\" />
						%s
					</head>
					<body>
						<div class=\"container-fluid\">
							<div class=\"navbar navbar-inverse\">
								<div class=\"navbar-inner\">
								<ul class=\"nav\">
									<li><a class=\"brand\" href=\"index.html\"><span class=\"wow\">Public</span> <span class=\"wee\">Action</span></a></li>
									<li><a href=\"%s\">%s</a></li>
								</ul>
								</div>
						</div>
						</div>
					<div class=\"container-fluid\">
						<article>"
						
						(if (= homeLinks true)
								(str
								"<div class=\"page-header\">
									<h4 class=\"h4\">" (:title docNode)  "</h4>
									<div class=\"pull-right\"><a href=\"index.html\"><i class=\"icon-home\"></i> - Home</a></div>
								</div>")
						)
						"%s
						</article>"
							(if (= homeLinks true)
								"<div class=\"pull-right\">
									<a href=\"index.html\"><i class=\"icon-home\"></i> - Home</a>
								</div>"
							)
							"
						</div>
						%s
						</body>
					</html>"
				) ; end of str 
				
					
				template-result
						(format templateStr 
							(:title docNode)
							meta-tags
							style
							contactBoxJS
							href 
							linkText
							(:body docNode)
							docReadyJS
						)
			]	
			template-result
			
		) ; end of let
		
)
