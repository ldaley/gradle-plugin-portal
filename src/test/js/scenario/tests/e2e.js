describe("root controller", function() {

    beforeEach(function () {
        console.log($httpBackend);
        browser().navigateTo('/index.html');
    });
    it('should render the response', function () {
        expect(element('.bar').text().tobe('bar'));
    });
});