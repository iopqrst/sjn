<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-cn">
	<head>
		<meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<title>Welcome !</title>

		<link href="/css/all.css" type="text/css" rel="stylesheet"/>
		<!--[if lt IE 9]>
	      <script src="js/html5shiv.min.js"></script>
	      <script src="js/respond.min.js"></script>
	    <![endif]-->

		<style type="text/css">
			body {
				min-height: 2000px;
			}
			
			/* MARKETING CONTENT -------------------------------------------------- */
				/* Center align the text within the three columns below the carousel */
			.marketing .col-lg-4 {
				margin-bottom: 20px;
				text-align: center;
			}
			
			.marketing h2 {
				font-weight: normal;
			}
			
			.marketing .col-lg-4 p {
				margin-right: 10px;
				margin-left: 10px;
			}
			
			/* Featurettes ------------------------- */
			.featurette-divider {
				margin: 80px 0; /* Space out the Bootstrap <hr> more */
			}
			
			/* Thin out the marketing headings */
			.featurette-heading {
				font-weight: 300;
				line-height: 1;
				letter-spacing: -1px;
			}
			
			/* RESPONSIVE CSS -------------------------------------------------- */
			@media ( min-width : 768px) { /* Navbar positioning foo */
				.navbar-wrapper {
					margin-top: 20px;
				}
				.navbar-wrapper .container {
					padding-right: 15px;
					padding-left: 15px;
				}
				.navbar-wrapper .navbar {
					padding-right: 0;
					padding-left: 0;
				}
				/* The navbar becomes detached from the top, so we round the corners */
				.navbar-wrapper .navbar {
					border-radius: 4px;
				}
				/* Bump up size of carousel content */
				.carousel-caption p {
					margin-bottom: 20px;
					font-size: 21px;
					line-height: 1.4;
				}
				.featurette-heading {
					font-size: 50px;
				}
			}
			
			@media ( min-width : 992px) {
				.featurette-heading {
					margin-top: 120px;
				}
			}
		</style>
	</head>
	<body>

		<div class="navbar navbar-inverse navbar-fixed-top" role="navigation">
			<div class="container">
				<div class="navbar-header">
					<button type="button" class="navbar-toggle collapsed"
						data-toggle="collapse" data-target=".navbar-collapse">
						<span class="sr-only">Toggle navigation</span>
						<span class="icon-bar"></span>
						<span class="icon-bar"></span>
						<span class="icon-bar"></span>
					</button>
					<a class="navbar-brand" href="#">Project name</a>
				</div>
				<div class="navbar-collapse collapse">
					<form class="navbar-form navbar-right" role="form">
						<div class="form-group">
							<input type="text" placeholder="Email" class="form-control">
						</div>
						<div class="form-group">
							<input type="password" placeholder="Password"
								class="form-control">
						</div>
						<button type="submit" class="btn btn-success">
							Sign in
						</button>
					</form>
				</div>
				<!--/.navbar-collapse -->
			</div>
		</div>

		<!-- Main jumbotron for a primary marketing message or call to action -->
		<div class="jumbotron">
			<div class="container">
				<h1>
					Hello, world!
				</h1>
				<p>
					This is a template for a simple marketing or informational website.
					It includes a large callout called a jumbotron and three supporting
					pieces of content. Use it as a starting point to create something
					more unique.
				</p>
				<p>
					<a class="btn btn-primary btn-lg" role="button">Learn more
						&raquo;</a>
				</p>
			</div>
		</div>

		<div class="container">
			<!-- Example row of columns -->
			<div class="row">
				<div class="col-md-4">
					<h2>
						Heading
					</h2>
					<p>
						Donec id elit non mi porta gravida at eget metus. Fusce dapibus,
						tellus ac cursus commodo, tortor mauris condimentum nibh, ut
						fermentum massa justo sit amet risus. Etiam porta sem malesuada
						magna mollis euismod. Donec sed odio dui.
					</p>
					<p>
						<a class="btn btn-default" href="#" role="button">View details
							&raquo;</a>
					</p>
				</div>
				<div class="col-md-4">
					<h2>
						Heading
					</h2>
					<p>
						Donec id elit non mi porta gravida at eget metus. Fusce dapibus,
						tellus ac cursus commodo, tortor mauris condimentum nibh, ut
						fermentum massa justo sit amet risus. Etiam porta sem malesuada
						magna mollis euismod. Donec sed odio dui.
					</p>
					<p>
						<a class="btn btn-default" href="#" role="button">View details
							&raquo;</a>
					</p>
				</div>
				<div class="col-md-4">
					<h2>
						Heading
					</h2>
					<p>
						Donec sed odio dui. Cras justo odio, dapibus ac facilisis in,
						egestas eget quam. Vestibulum id ligula porta felis euismod
						semper. Fusce dapibus, tellus ac cursus commodo, tortor mauris
						condimentum nibh, ut fermentum massa justo sit amet risus.
					</p>
					<p>
						<a class="btn btn-default" href="#" role="button">View details
							&raquo;</a>
					</p>
				</div>
			</div>

			<hr>

			<!-- Marketing messaging and featurettes
    ================================================== -->
			<!-- Wrap the rest of the page in another container to center all the content. -->

			<div class="container marketing">

				<!-- Three columns of text below the carousel -->
				<div class="row">
					<div class="col-lg-4">
						<img class="img-circle"
							src="data:image/gif;base64,R0lGODlhAQABAIAAAHd3dwAAACH5BAAAAAAALAAAAAABAAEAAAICRAEAOw=="
							alt="Generic placeholder image"
							style="width: 140px; height: 140px;">
						<h2>
							Heading
						</h2>
						<p>
							Donec sed odio dui. Etiam porta sem malesuada magna mollis
							euismod. Nullam id dolor id nibh ultricies vehicula ut id elit.
							Morbi leo risus, porta ac consectetur ac, vestibulum at eros.
							Praesent commodo cursus magna.
						</p>
						<p>
							<a class="btn btn-default" href="#" role="button">View
								details &raquo;</a>
						</p>
					</div>
					<!-- /.col-lg-4 -->
					<div class="col-lg-4">
						<img class="img-circle"
							src="data:image/gif;base64,R0lGODlhAQABAIAAAHd3dwAAACH5BAAAAAAALAAAAAABAAEAAAICRAEAOw=="
							alt="Generic placeholder image"
							style="width: 140px; height: 140px;">
						<h2>
							Heading
						</h2>
						<p>
							Duis mollis, est non commodo luctus, nisi erat porttitor ligula,
							eget lacinia odio sem nec elit. Cras mattis consectetur purus sit
							amet fermentum. Fusce dapibus, tellus ac cursus commodo, tortor
							mauris condimentum nibh.
						</p>
						<p>
							<a class="btn btn-default" href="#" role="button">View
								details &raquo;</a>
						</p>
					</div>
					<!-- /.col-lg-4 -->
					<div class="col-lg-4">
						<img class="img-circle"
							src="data:image/gif;base64,R0lGODlhAQABAIAAAHd3dwAAACH5BAAAAAAALAAAAAABAAEAAAICRAEAOw=="
							alt="Generic placeholder image"
							style="width: 140px; height: 140px;">
						<h2>
							Heading
						</h2>
						<p>
							Donec sed odio dui. Cras justo odio, dapibus ac facilisis in,
							egestas eget quam. Vestibulum id ligula porta felis euismod
							semper. Fusce dapibus, tellus ac cursus commodo, tortor mauris
							condimentum nibh, ut fermentum massa justo sit amet risus.
						</p>
						<p>
							<a class="btn btn-default" href="#" role="button">View
								details &raquo;</a>
						</p>
					</div>
					<!-- /.col-lg-4 -->
				</div>
				<!-- /.row -->


				<!-- START THE FEATURETTES -->

				<hr class="featurette-divider">

				<div class="row featurette">
					<div class="col-md-7">
						<h2 class="featurette-heading">
							First featurette heading.
							<span class="text-muted">It'll blow your mind.</span>
						</h2>
						<p class="lead">
							Donec ullamcorper nulla non metus auctor fringilla. Vestibulum id
							ligula porta felis euismod semper. Praesent commodo cursus magna,
							vel scelerisque nisl consectetur. Fusce dapibus, tellus ac cursus
							commodo.
						</p>
					</div>
					<div class="col-md-5">
						<img class="featurette-image img-responsive"
							data-src="holder.js/500x500/auto" alt="Generic placeholder image">
					</div>
				</div>

				<hr class="featurette-divider">

				<div class="row featurette">
					<div class="col-md-5">
						<img class="featurette-image img-responsive"
							data-src="holder.js/500x500/auto" alt="Generic placeholder image">
					</div>
					<div class="col-md-7">
						<h2 class="featurette-heading">
							Oh yeah, it's that good.
							<span class="text-muted">See for yourself.</span>
						</h2>
						<p class="lead">
							Donec ullamcorper nulla non metus auctor fringilla. Vestibulum id
							ligula porta felis euismod semper. Praesent commodo cursus magna,
							vel scelerisque nisl consectetur. Fusce dapibus, tellus ac cursus
							commodo.
						</p>
					</div>
				</div>

				<hr class="featurette-divider">

				<div class="row featurette">
					<div class="col-md-7">
						<h2 class="featurette-heading">
							And lastly, this one.
							<span class="text-muted">Checkmate.</span>
						</h2>
						<p class="lead">
							Donec ullamcorper nulla non metus auctor fringilla. Vestibulum id
							ligula porta felis euismod semper. Praesent commodo cursus magna,
							vel scelerisque nisl consectetur. Fusce dapibus, tellus ac cursus
							commodo.
						</p>
					</div>
					<div class="col-md-5">
						<img class="featurette-image img-responsive"
							data-src="holder.js/500x500/auto" alt="Generic placeholder image">
					</div>
				</div>

				<hr class="featurette-divider">

				<!-- /END THE FEATURETTES -->


				<!-- FOOTER -->
				<footer>
				<p class="pull-right">
					<a href="#">回到顶部</a>
				</p>
				<p>
					&copy; 2014 Company, Inc. &middot;
					<a href="#">Privacy</a> &middot;
					<a href="#">Terms</a>
				</p>
				</footer>

			</div>
		</div>
		<!-- /container -->
	</body>
</html>
