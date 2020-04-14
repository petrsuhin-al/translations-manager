package constants;

public interface Constants {
  String[] excludedFolders = {
    "node_modules", ".vscode", ".idea", "e2e", ".gitlab", "k8s"
  };
  String[] excludedFiles = {
    "tslint.json", "package.json", "package-lock.json",
    "package.json", "tsconfig", "angular.json"
  };
}
