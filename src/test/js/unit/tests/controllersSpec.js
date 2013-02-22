describe("root controller", function() {
    beforeEach(angular.mock.module('controllers'));

    beforeEach(inject(function($rootScope, $controller) {
        this.scope = $rootScope.$new();
        $controller('main', {
            $rootScope: this.scope
        });
    }));

    it("should set title", function() {
        expect(this.scope.title).toBe("Gradle Plugins");
    });
});
