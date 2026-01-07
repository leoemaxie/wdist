{
  description = "My Java development shell";
  inputs.nixpkgs.url = "nixpkgs/nixpkgs-unstable";
  outputs = inputs: let
    system = "x86_64-linux";
    pkgs = inputs.nixpkgs.legacyPackages.${system};
  in {
    devShell.${system} = pkgs.mkShell rec {
      buildInputs = with pkgs; [ jdk17 ];

      shellHook = ''
        export JAVA_HOME=${pkgs.jdk17}
        export PATH=$JAVA_HOME/bin:$PATH
      '';
    };
  };
}
