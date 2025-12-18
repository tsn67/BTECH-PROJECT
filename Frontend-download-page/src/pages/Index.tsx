import { Download, Moon, Sun, MonitorIcon, Terminal, ExternalLink } from "lucide-react";
import { Button } from "@/components/ui/button";
import { useTheme } from "@/hooks/useTheme";
import useDownloadWindowsClient from "@/hooks/useDownloadWindowsClient";

const Index = () => {
  const { theme, toggleTheme } = useTheme();

  return (
    <div className="h-screen bg-background flex flex-col p-6 md:p-10">
      {/* Header */}
      <header className="flex items-center justify-between mb-8">
        <div className="flex items-center gap-2">
          <span className="text-sm font-semibold">MediVault</span>
          <span className="text-xs text-muted-foreground">v1.0.0</span>
        </div>
        <button
          onClick={toggleTheme}
          className="p-2 rounded-lg hover:bg-secondary transition-colors"
        >
          {theme === "dark" ? <Sun className="h-4 w-4" /> : <Moon className="h-4 w-4" />}
        </button>
      </header>

      {/* Main Content */}
      <main className="flex-1 flex flex-col lg:flex-row gap-6 min-h-0">
        {/* Download Section */}
        <div className="window-card flex-1 flex flex-col">
          <div className="window-header">
            <div className="window-dot bg-red-500" />
            <div className="window-dot bg-yellow-500" />
            <div className="window-dot bg-green-500" />
            <span className="ml-2 text-xs text-muted-foreground">download</span>
          </div>
          <div className="flex-1 p-6 flex flex-col justify-center">
            <h1 className="text-2xl font-bold mb-2">Service Client</h1>
            <p className="text-sm text-muted-foreground mb-8">
              Download MediVault for your platform
            </p>

            <div className="space-y-3">
              {/* Windows */}
              <button onClick={() => useDownloadWindowsClient('https://localhost:8081/api/files/docker-images/ether-client-v-1.tar')} className="w-full flex items-center gap-4 p-4 rounded-lg bg-secondary hover:bg-secondary/80 transition-colors text-left">
                <MonitorIcon className="h-5 w-5 text-primary" />
                <div className="flex-1">
                  <div className="text-sm font-medium">Windows</div>
                  <div className="text-xs text-muted-foreground">x64 · 85MB · .exe</div>
                </div>
                <Download className="h-4 w-4" />
              </button>

              {/* Linux */}
              <button
                disabled
                className="w-full flex items-center gap-4 p-4 rounded-lg bg-secondary/50 text-left opacity-50 cursor-not-allowed"
              >
                <Terminal className="h-5 w-5 text-muted-foreground" />
                <div className="flex-1">
                  <div className="text-sm font-medium text-muted-foreground">Linux</div>
                  <div className="text-xs text-muted-foreground">coming soon</div>
                </div>
              </button>
            </div>
          </div>
        </div>

        {/* Requirements Section */}
        <div className="window-card flex-1 flex flex-col">
          <div className="window-header">
            <div className="window-dot bg-red-500" />
            <div className="window-dot bg-yellow-500" />
            <div className="window-dot bg-green-500" />
            <span className="ml-2 text-xs text-muted-foreground">requirements</span>
          </div>
          <div className="flex-1 p-6 overflow-auto">
            <div className="space-y-6 text-sm">
              <div>
                <div className="font-medium mb-1">Network</div>
                <div className="text-muted-foreground text-xs space-y-1">
                  <p>• Internet connection ≥ 30 Mbps</p>
                  <p>• Ports exposed to public network</p>
                </div>
              </div>

              <div>
                <div className="font-medium mb-1">Docker Required</div>
                <div className="text-muted-foreground text-xs mb-3">
                  MediVault runs in Docker containers
                </div>
                <div className="space-y-2">
                  <a
                    href="https://docs.docker.com/desktop/install/windows-install/"
                    target="_blank"
                    rel="noopener noreferrer"
                    className="flex items-center gap-2 text-xs text-primary hover:underline"
                  >
                    <ExternalLink className="h-3 w-3" />
                    Docker Desktop for Windows
                  </a>
                  <a
                    href="https://docs.docker.com/engine/install/ubuntu/"
                    target="_blank"
                    rel="noopener noreferrer"
                    className="flex items-center gap-2 text-xs text-primary hover:underline"
                  >
                    <ExternalLink className="h-3 w-3" />
                    Docker Engine for Ubuntu
                  </a>
                </div>
              </div>

              <div className="pt-4 border-t border-border">
                <code className="text-xs text-muted-foreground bg-secondary px-2 py-1 rounded">
                  $ docker --version
                </code>
              </div>
            </div>
          </div>
        </div>
      </main>

      {/* Footer */}
      <footer className="mt-6 text-center">
        <p className="text-xs text-muted-foreground">
          © {new Date().getFullYear()} MediVault
        </p>
      </footer>
    </div>
  );
};

export default Index;
