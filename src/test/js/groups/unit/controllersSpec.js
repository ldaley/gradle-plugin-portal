describe("Controllers - ", function() {
    beforeEach(angular.mock.module('controllers'));

    beforeEach(inject(function($rootScope, $controller) {
        this.rootScope = $rootScope;
        $controller('main', {
            $rootScope: $rootScope
        });
    }));

    it("should set title", function() {
        expect(this.rootScope.title).toBe("Gradle Plugins");
    });
});