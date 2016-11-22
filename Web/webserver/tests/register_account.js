module.exports = {
  'Go to Registration': function(browser) {
    browser
      .url('http://checkedout.ca/')
      .waitForElementVisible('body', 1000)
      .click('#account')
      .pause(500)
      .click('#register')
      .pause(500)
      .waitForElementVisible('.Register', 1000)
      .end()
   }
};