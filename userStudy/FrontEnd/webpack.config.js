var path = require('path')
var babelrc = JSON.parse(require('fs').readFileSync('.babelrc').toString())
var pkg = require('./package.json')

var webpack = require('webpack')
var ignore = [
    new webpack.IgnorePlugin(/react\/addons/),
    new webpack.IgnorePlugin(/react\/lib\/ReactContext/),
    new webpack.IgnorePlugin(/react\/lib\/ExecutionEnvironment/)
    ]

module.exports = {
    entry: pkg.main,
    output: { path: path.resolve(__dirname, 'dist'), filename: 'bundle.js' },
    devtool: 'source-map',
    stats: { colors: true },
    node: { fs: 'empty'},
    plugins: ignore,
    module: {
        loaders: [
        {
            test: /.jsx?$/,
            loader: 'babel-loader',
            include: path.resolve(__dirname, 'src'),
            query: babelrc
        }, {
            test: /\.json$/,
            loader: 'json'
        }, {
          test: /\.css$/,
          loader: 'style!css-loader?sourceMap'
        }, {
          test: /\.woff(\?v=\d+\.\d+\.\d+)?$/,
          loader: "url-loader?limit=10000&mimetype=application/font-woff"
        }, {
          test: /\.woff2(\?v=\d+\.\d+\.\d+)?$/,
          loader: "url-loader?limit=10000&mimetype=application/font-woff"
        }, {
          test: /\.ttf(\?v=\d+\.\d+\.\d+)?$/,
          loader: "url-loader?limit=10000&mimetype=application/octet-stream"
        }, {
          test: /\.eot(\?v=\d+\.\d+\.\d+)?$/,
          loader: "file"
        }, {
          test: /\.svg(\?v=\d+\.\d+\.\d+)?$/,
          loader: "url-loader?limit=10000&mimetype=image/svg+xml"
        }
    ]
  }
}