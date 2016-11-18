module.exports = {
  'Enter Item': function(browser) {
    browser
      .url('localhost:8080')
      .waitForElementVisible('body', 1000)
      .setValue('input[type=text]', 'apple')
      .end()
  }
};