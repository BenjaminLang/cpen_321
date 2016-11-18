module.exports = {
  'Title Test': function(browser) {
    browser
      .url('localhost:8080')
      .waitForElementVisible('body')
      .assert.title('Home')
      // .saveScreenshot('ginea-pig-test.png')
      .end();
  }
};